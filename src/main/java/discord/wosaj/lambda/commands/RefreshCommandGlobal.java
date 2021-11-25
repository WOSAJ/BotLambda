package discord.wosaj.lambda.commands;

import discord.wosaj.lambda.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class RefreshCommandGlobal extends Command {
    public RefreshCommandGlobal() {
        super("refresh_global",
                true,
                false,
                "Refresh the initial script for ALL servers");
    }

    @Override
    public void run(@Nonnull MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(System.getenv("OWNER"))) {
            event.getTextChannel().sendMessage("You aren't the bot owner!").queue();
            return;
        }
        for(Guild guild : event.getJDA().getGuilds()) {
            Main.refresh(guild.getId());
        }
        event.getTextChannel().sendMessage("Refreshed!").queue();
    }

    @Override
    @SuppressWarnings("all")
    public void runAsSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.getMember().getId().equals(System.getenv("OWNER"))) {
            event.reply("You aren't the bot owner!").queue();
            return;
        }
        for(Guild guild : event.getJDA().getGuilds()) {
            Main.refresh(guild.getId());
        }
        String ids = "";
        for (Guild guild : event.getJDA().getGuilds()) {
            ids += guild.getId() + "\n";
        }

        event.reply("Refreshed on:\n" + ids).queue();
    }
}
