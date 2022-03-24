package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.database.guild.GuildItem;
import tk.wosaj.lambda.database.guild.GuildService;
import tk.wosaj.lambda.util.GuildDataSettings;
import tk.wosaj.lambda.util.Strainer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@DisableCommand.AlwaysEnabled
@SuppressWarnings("unused")
public final class EnableCommand extends Command {
    public EnableCommand() {
        super(
                "enable",
                "Enables disabled command",
                true,
                Strainer.ADMIN,
                new Argument(
                    OptionType.STRING,
                    "command-name",
                    "Name of command",
                    false)
        );
    }

    @Override
    public void execute(@Nonnull MessageReceivedEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(event.getGuild().getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = splitMessage(event.getMessage().getContentRaw())[1];
        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.remove(asString);
            item.setJson(settings.toJson(false));
            service.update(item);

            for (Command command : Main.manager.getCommands()) {
                if(command.getName().equals(asString)) {
                    Main.manager.registerCommand(command, event.getGuild());
                    reply(event, "<:accepted:911605951335915530> Command Successfully enabled!");
                }
            }
            return;
        }
        reply(event, "<:canceled:934419252495130744> Can't find command!");
    }

    @Override
    public void executeAsSlash(@Nonnull SlashCommandInteractionEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(Objects.requireNonNull(event.getGuild()).getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = Objects.requireNonNull(event.getOption("command-name")).getAsString();
        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.remove(asString);
            item.setJson(settings.toJson(false));
            service.update(item);
            for (Command command : Main.manager.getCommands()) {
                if(command.getName().equals(asString)) {
                    Main.manager.registerCommand(command, event.getGuild());
                    reply(event, "<:accepted:911605951335915530> Command Successfully enabled!");
                }
            }
            return;
        }
        reply(event, "<:canceled:934419252495130744> Can't find command!");
    }
}
