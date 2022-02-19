package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.database.guild.GuildItem;
import tk.wosaj.lambda.database.guild.GuildService;
import tk.wosaj.lambda.util.GuildDataSettings;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class DisableCommand extends Command {
    public DisableCommand() {
        super("disable", "Disables command by name", true, new Argument(
                OptionType.STRING,
                "command-name",
                "Name of command",
                false
        ));
    }

    public DisableCommand(String name, String description, boolean ephemeral, Argument... arguments) {
        super(name, description, ephemeral, arguments);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(event.getGuild().getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = splitMessage(event.getMessage().getContentRaw())[1];
        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && !settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.add(asString);
            item.setJson(settings.toJson(false));
            service.update(item);
            Main.manager.removeCommand(event.getGuild(), asString);
            reply(event, "<:accepted:911605951335915530> Command Successfully disabled!");
        }
        reply(event, "<:canceled:934419252495130744> Can't find command!");
    }

    @Override
    public void executeAsSlash(@Nonnull SlashCommandEvent event) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(Objects.requireNonNull(event.getGuild()).getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        String asString = Objects.requireNonNull(event.getOption("command-name")).getAsString();
        List<String> collect =
                Main.manager.getCommands().stream().map(Command::getName).collect(Collectors.toList());
        if (collect.contains(asString) && !settings.blacklistCommands.contains(asString)) {
            settings.blacklistCommands.add(asString);
            item.setJson(settings.toJson(false));
            service.update(item);
            Main.manager.removeCommand(event.getGuild(), asString);
            reply(event, "<:accepted:911605951335915530> Command Successfully disabled!");
        }
        reply(event, "<:canceled:934419252495130744> Can't find command!");
    }
}
