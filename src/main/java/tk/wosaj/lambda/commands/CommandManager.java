package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.database.guild.GuildItem;
import tk.wosaj.lambda.database.guild.GuildService;
import tk.wosaj.lambda.database.guild.GuildUtil;
import tk.wosaj.lambda.util.AutoSearchable;
import tk.wosaj.lambda.util.Exclude;
import tk.wosaj.lambda.util.GuildDataSettings;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CommandManager extends ListenerAdapter implements AutoSearchable {
    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private static final Reflections reflections = new Reflections();
    private final JDA jda;
    private final String defaultPrefix;
    private final List<Command>
            commands = new ArrayList<>(),
            normalCommands = new ArrayList<>(),
            defaultCommands = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(32, r -> {
                Thread thread = new Thread(r);
                thread.setUncaughtExceptionHandler((t, e) -> {
                    if(!(e instanceof IllegalMonitorStateException))
                        LoggerFactory.getLogger(t.getName()).error("Exception in thread " + t.getName(), t);
                });
                return thread;
            });

    public CommandManager(@Nonnull JDA jda, String defaultPrefix) {
        this.jda = jda;
        this.defaultPrefix = defaultPrefix;
        initCommands();
        updateNormalCommands();
        updateDefaultCommands();
        jda.addEventListener(this);
    }

    public JDA getJda() {
        return jda;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public void initCommands() {
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        root: for (Class<? extends Command> aClass : classes) {
            try {
                if(aClass.getAnnotation(Exclude.class) != null) {
                    for (Class<? extends AutoSearchable> clazz : aClass.getAnnotation(Exclude.class)
                            .value()) {
                        if (!clazz.getName().equals(getClass().getName())) continue root;
                        break;
                    }
                }
                commands.add(aClass.newInstance());
                logger.debug("Added command: {}", aClass.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateNormalCommands() {
        normalCommands.clear();
        normalCommands.addAll(commands.stream().filter(Command::isNormal).collect(Collectors.toList()));
    }

    private void updateDefaultCommands() {
        defaultCommands.clear();
        defaultCommands.addAll(commands.stream().filter(Command::byDefault).collect(Collectors.toList()));
    }

    public void registerCommand(@Nonnull Command command, Guild guild) {
        if (!command.isSlash()) return;
        guild.retrieveCommands().queue(list -> {
            if (list.stream()
                .map(net.dv8tion.jda.api.interactions.commands.Command::getName)
                .collect(Collectors.toList()).contains(command.getName())) return;
            CommandCreateAction action = guild.upsertCommand(command.getName(), command.getDescription());
            for (Argument argument : command.getArguments()) {
                action = action.addOption(
                    argument.getType(),
                    argument.getName(),
                    argument.getDescription(),
                    !argument.isOptional());
            }
            action.queue();
        });
    }

    public void removeCommand(@Nonnull Guild guild, String commandName) {
        guild.retrieveCommands().queue(list -> {
            for (net.dv8tion.jda.api.interactions.commands.Command command : list) {
                if (command.getName().equals(commandName)) {
                    guild.deleteCommandById(command.getId()).queue();
                    return;
                }
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getMember() == null) return;
        executor.submit(() -> {
            try {
                for (Command command : commands) {
                    if(command.getName().equals(event.getName())) {
                        event.deferReply(command.ephemeral).setEphemeral(command.ephemeral).queue();
                            List<String> blacklistCommands = GuildDataSettings.loadFromJson(
                                new GuildService().get(
                                    GuildUtil.generateDatabaseName(
                                         Objects.requireNonNull(
                                             event.getGuild()
                                         ).getId())).getJson()
                            ).blacklistCommands;
                            for (String blacklistCommand : blacklistCommands)
                                if(blacklistCommand.equals(event.getName())) {
                                    event.getHook().sendMessage(
                                         Main.emotes.get("canceled") + " Command disabled")
                                        .setEphemeral(true).queue();
                                    return;
                                }
                        if(command.getAccepter().check(event.getMember())) {
                            Command.reply(event, Main.emotes.get("canceled") + " You cant use it!", true);
                            return;
                        }
                        command.executeAsSlash(event);
                        if(command.requireGC()) System.gc();
                        return;
                    }
                }
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            event.reply("Unknown error").setEphemeral(true).queue();
        });
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMember() == null) {
            return;
        }
        executor.submit(() -> {
            String prefix = defaultPrefix;
            GuildDataSettings settings = GuildDataSettings.load(event.getGuild().getId());
            if(settings.prefix != null) {
                prefix = settings.prefix;
            }
            try {
                for (Command command : normalCommands) {
                    if (event.getMessage().getContentRaw().startsWith(prefix + command.getName())) {
                        for (String blacklistCommand : settings.blacklistCommands)
                            if (blacklistCommand.equals(command.getName())) {
                                return;
                            }
                        if (!command.getAccepter().check(event.getMember())) {
                            Command.reply(event, Main.emotes.get("canceled") + " You cant use it!");
                            return;
                        }
                        command.execute(event);
                        if(command.requireGC()) System.gc();
                        break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                Command.reply(event, Main.emotes.get("canceled") + " Invalid arguments");
                //TODO Automatic help print
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        executor.submit(() -> {
            try {
                GuildDataSettings settings = new GuildDataSettings(event.getGuild());
                if(event.getGuild().getCommunityUpdatesChannel() != null) {
                    settings.techChannel = event.getGuild().getCommunityUpdatesChannel().getId();
                }
                GuildService service = new GuildService();
                service.save(
                        new GuildItem(GuildUtil.generateDatabaseName(event.getGuild().getId()), settings)
                );

                defaultCommands.forEach(command -> registerCommand(command, event.getGuild()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        executor.submit(() -> {
            try {
                GuildService service = new GuildService();
                GuildItem item = service.get(GuildUtil.generateDatabaseName(event.getGuild().getId()));
                logger.info(service.get(GuildUtil.generateDatabaseName(event.getGuild().getId())).toString());
                service.delete(item);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<Command> getNormalCommands() {
        return normalCommands;
    }

    public List<Command> getDefaultCommands() {
        return defaultCommands;
    }

    public void shutdown() {
        if(!executor.isShutdown()) executor.shutdown();
    }
}
