package discord.wosaj.lambda.commands;

import discord.wosaj.lambda.Main;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class RefreshCommand extends Command {
    public RefreshCommand() {
        super("refresh",
                true,
                false,
                "Refresh the initial script for this server");
    }

    @Override
    public void run(@Nonnull MessageReceivedEvent event) {
        Main.refresh(event.getGuild().getId());
        event.getTextChannel().sendMessage("Refreshed!").queue();
    }

    @Override
    @SuppressWarnings("all")
    public void runAsSlashCommand(@Nonnull SlashCommandEvent event) {
        Main.refresh(event.getGuild().getId());
        event.reply("Refreshed!").queue();
    }

}
