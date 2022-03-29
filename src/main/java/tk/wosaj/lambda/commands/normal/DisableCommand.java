package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.reflections.Reflections;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.database.guild.GuildItem;
import tk.wosaj.lambda.database.guild.GuildService;
import tk.wosaj.lambda.util.Accepter;
import tk.wosaj.lambda.util.GuildDataSettings;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@DisableCommand.AlwaysEnabled
@SuppressWarnings("unused")
public final class DisableCommand extends Command {
    private static final List<String> alwaysEnabled = new ArrayList<>();
    static {
        Set<Class<?>> typesAnnotatedWith = new Reflections().getTypesAnnotatedWith(AlwaysEnabled.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            try {
                Object o = aClass.newInstance();
                if(!(o instanceof Command)) continue;
                alwaysEnabled.add(((Command) o).getName());
            } catch (Exception ignored) {}
        }
    }
    public DisableCommand() {
        super("disable", "Disables command by name");
        arguments.add(new Argument(OptionType.STRING, "command-name", "Name of command", false));
        ephemeral = true;
        accepter = Accepter.ADMIN;
    }

    @Override
    public void execute(@Nonnull MessageReceivedEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(event.getGuild().getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = splitMessage(event.getMessage().getContentRaw())[1];

        if(alwaysEnabled.contains(asString)) {
            reply(event, Main.emotes.get("canceled") + " Command can't disabled!");
            return;
        }

        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && !settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.add(asString);
            item.setJson(settings.toJson(false));
            service.update(item);
            Main.manager.removeCommand(event.getGuild(), asString);
            reply(event, Main.emotes.get("accepted") + " Command Successfully disabled!");
            return;
        }
        reply(event, Main.emotes.get("canceled") + " Can't find command!");
    }

    @Override
    public void executeAsSlash(@Nonnull SlashCommandInteractionEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(Objects.requireNonNull(event.getGuild()).getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = Objects.requireNonNull(event.getOption("command-name")).getAsString();

        if(alwaysEnabled.contains(asString)) {
            reply(event, Main.emotes.get("canceled") + " Command can't disabled!");
            return;
        }

        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && !settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.add(asString);
            item.setJson(settings.toJson(false));
            service.update(item);
            Main.manager.removeCommand(event.getGuild(), asString);
            reply(event, Main.emotes.get("accepted") + " Command Successfully disabled!");
            return;
        }
        reply(event, Main.emotes.get("canceled") + " Can't find command!");
    }
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AlwaysEnabled {}
}
