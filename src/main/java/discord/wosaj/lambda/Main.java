package discord.wosaj.lambda;

import discord.wosaj.lambda.json.JsonManager;
import discord.wosaj.lambda.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;
//import java.util.logging.Logger;

public class Main {

    //public static final Logger MAIN_LOGGER = Logger.getLogger("Main logs");

    public static final Command[] commands = {
            new HelpCommand(),
            new RefreshCommand(),
            new RefreshCommandGlobal(),
            new DeleteCommand()
    };

    public static JDA jda;
    static CommandDriver driver = new CommandDriver();

    public static void main(String[] args) throws LoginException, InterruptedException {

        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .addEventListeners(new MessageListener(), driver)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.competing("/help for help"))
                .build();

        jda.awaitReady();

        driver.registerCommand(new RefreshCommandGlobal(), jda.getGuildById("903420089838223370"));

    }
    public static class MessageListener extends ListenerAdapter  {

        @Override
        public void onGuildJoin(@Nonnull GuildJoinEvent event) {
            refresh(event.getGuild().getId());
        }

        @Override
        public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
                if (!event.getTextChannel().equals(jda.getTextChannelById("903573481201995786")) &&
                        !event.getGuild().equals(jda.getGuildById("876329449975595009")))
                    Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById("903420089838223370"))
                        .getTextChannelById("903573481201995786"))
                        .sendMessage(
                     String.format("[*%s*] (**%s**) - **%s**: %s",
                        new Date(),
                             event.getGuild().getName(),
                             event.getAuthor().getName(),
                             event.getMessage())
                        ).submit();
        }
    }

    public static void refresh(@Nonnull String guildId) {
        try {
            if(!new JsonManager().isSettingsFileExist(guildId)) {
                new JsonManager().createSettingsFile(guildId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Command command : commands) {
            driver.registerCommand(command, jda.getGuildById(guildId));
        }
    }
}


