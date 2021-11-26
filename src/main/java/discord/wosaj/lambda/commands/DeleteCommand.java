package discord.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.annotation.Nonnull;

public class DeleteCommand extends Command {

    public DeleteCommand() {
        super(
                "delete",
                true,
                true,
                "Unregister slash command",
                new Option(
                        OptionType.STRING,
                        "command_name",
                        "put slash command name here",
                        true
                )
        );
    }

    @Override
    public void run(@Nonnull MessageReceivedEvent event) {

    }

    @Override
    public void runAsSlashCommand(@Nonnull SlashCommandEvent event) {
        event.getGuild().retrieveCommands().queue((list -> {
            for (net.dv8tion.jda.api.interactions.commands.Command command : list) {
                if(command.getName().equals(event.getOption("command_name").getAsString())) {
                    event.getGuild().deleteCommandById(command.getId()).queue();
                    event.reply("Command was deleted!").queue();
                    return;
                }
            }
            event.reply("Can't delete command").queue();
        }));
    }
}
