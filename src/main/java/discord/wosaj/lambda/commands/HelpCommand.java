package discord.wosaj.lambda.commands;

import discord.wosaj.lambda.json.JsonManager;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.io.IOException;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", true, false, "Sends help page");
    }

    @Override
    public void run(@Nonnull MessageReceivedEvent event) {
        try {
            String prefix = new JsonManager().getPrefix(event.getGuild().getId());
            event.getTextChannel().sendMessage(String.format(
                    new JsonManager().getSettings(event.getGuild().getId()).getHelp(), prefix
                    )).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("all")
    public void runAsSlashCommand(@Nonnull SlashCommandEvent event) {
        try {
            String prefix = new JsonManager().getPrefix(event.getGuild().getId());
            event.reply(String.format(
                    new JsonManager().getSettings(event.getGuild().getId()).getHelp(), prefix
            )).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}