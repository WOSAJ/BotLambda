package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.reflections.Reflections;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class CommandManager extends ListenerAdapter {
    private final JDA jda;
    private final String defaultPrefix;
    protected final ArrayList<String> names = new ArrayList<>();
    protected final ArrayList<Command> commands = new ArrayList<>();
    protected final ArrayList<Command> defaultToReg = new ArrayList<>();
    public static final Class<? extends Annotation> annotation = RegisterCommand.class;

    public CommandManager(JDA jda, String defaultPrefix) {
        this.jda = jda;
        this.defaultPrefix = defaultPrefix;
    }

    public JDA getJDA() {
        return jda;
    }

    public void registerCommand(Command command, @Nonnull Guild guild) {
        guild.retrieveCommands().queue(list -> {
            for (net.dv8tion.jda.api.interactions.commands.Command command1 : list) {
                if(command1.getName().equals(command.getName())) return;
            }

            if(command.isSlash()) {
                CommandCreateAction action = guild.upsertCommand(
                        command.getName(),
                        command.getDescription()
                );
                if(command.hasArguments()) {
                    for (Argument argument : command.getArguments()) {
                        action = action.addOption(
                                argument.getType(),
                                argument.getName(),
                                argument.getDescription(),
                                !argument.isOptional());
                    }
                }
                action.queue();
            }
            if(command.isNormal()) names.add(command.getName());
        });
    }

    public void removeCommand(Command command, @Nonnull Guild guild) {
        guild.retrieveCommands().queue(list -> {
            for (net.dv8tion.jda.api.interactions.commands.Command command1 : list) {
                if(command1.getName().equals(command.getName())) {
                    guild.deleteCommandById(command1.getId()).queue();
                    commands.remove(command);
                    if(command.isNormal()) names.remove(command.getName());
                }
            }
        });
    }

    public Set<Class<?>> registerByAnnotation(Guild guild) {
        Reflections reflections = new Reflections();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        for (Class<?> aClass : classes) {
            try {
                Object instance = aClass.getConstructors()[0].newInstance();
                if(instance instanceof Command) {
                    Command command = (Command) instance;
                    registerCommand(command, guild);
                } else throw new IllegalArgumentException();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        System.out.println(commands);
        if (event.getGuild() != null) {
            for (Command command : commands) {
                if(command.getName().equals(event.getName())) {
                    command.executeAsSlash(event);
                    break;
                }
            }
        }
    }

    //REFACTOR Database prefix
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getMember() == null) return;
        String[] split = splitCommand(event.getMessage().getContentRaw(), defaultPrefix);
        if(split.length == 0) return;
        Command command = getForName(split[0]);
        if (command == null || !command.isNormal()) return;
        if(event.getMember().getUser().isBot()) {
            Commands.reply(event.getMessage(), "<:canceled:934419252495130744> F*ck you, bot!");
            return;
        }
        command.execute(event);
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        defaultToReg.forEach(command -> registerCommand(command, event.getGuild()));
    }

    @Nullable
    public Command getForName(String name) {
        for (Command command : commands) {
            if(name.equals(command.getName())) return command;
        }
        return null;
    }

    public void initRegisters() {
        Reflections reflections = new Reflections();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        for (Class<?> aClass : classes) {
            try {
                Object instance = aClass.getConstructors()[0].newInstance();
                if(!(instance instanceof Command)) throw new IllegalArgumentException();
                commands.add((Command) aClass.getConstructors()[0].newInstance());
                if(((RegisterCommand) aClass.getAnnotation(annotation)).byDefault()) {
                    defaultToReg.add(((Command) aClass.getConstructors()[0].newInstance()));
                }

            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void refresh(@Nonnull Guild guild) {
        Reflections reflections = new Reflections();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        classes.forEach(command1 -> {
            try {
                Object instance = command1.getConstructors()[0].newInstance();
                if(!(instance instanceof Command)) throw new IllegalArgumentException();
                registerCommand(((Command) instance), guild);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    @CheckReturnValue
    @Nonnull
    public static String[] splitCommand(@Nonnull String content, @Nonnull String prefix) {
        if(content.length() <= 0) return new String[0];
        String withoutPrefix = content.substring(prefix.length());
        return withoutPrefix.split(" ");
    }

    @Nonnull
    public static List<Command> getAnnotatedCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        Set<Class<?>> classes = new Reflections().getTypesAnnotatedWith(RegisterCommand.class);
        for (Class<?> aClass : classes) {
            try {
                Object instance = aClass.getConstructors()[0].newInstance();
                if(!(instance instanceof Command)) throw new IllegalArgumentException();
                commands.add((Command) instance);

            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return commands;
    }
}
