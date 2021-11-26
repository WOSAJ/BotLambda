package discord.wosaj.lambda.commands;

import discord.wosaj.lambda.Main;
import discord.wosaj.lambda.json.JsonManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

import javax.annotation.Nonnull;
import java.io.IOException;

public class CommandDriver extends ListenerAdapter {

    JsonManager manager = new JsonManager();

    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        for(Command command : Main.commands) {
            if(command.getName().equals(event.getName())) command.runAsSlashCommand(event);
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        try {
            String prefix = manager.getPrefix(event.getGuild().getId());


        String message = event.getMessage().getContentRaw().substring(prefix.length());

        for (Command command : Main.commands) {
            if(message.startsWith(command.getName())) command.run(event);
            }
        } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void registerCommand(@Nonnull Command command, @Nonnull Guild guild) {
        if (command.isSlash()) {
            guild.retrieveCommands().queue(list -> {
                CommandCreateAction action = guild.upsertCommand(
                        command.getName(),
                        command.getDescription()
                );
                if(command.isHasArguments()) {
                    for(Command.Option option : command.getOptions()) {
                        action = action.addOption(
                                option.getType(),
                                option.getName(),
                                option.getDescription(),
                                option.isRequired()
                        );
                    }
                }
                action.queue();
            });
        }
    }
}