package discord.wosaj;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.Objects;

public class Main {
    static JDA jda;
    static MessageListener lsn = new MessageListener();
    public static void main(String[] args) throws LoginException, InterruptedException {

        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .addEventListeners(lsn)
                .build();
        jda.awaitReady();

    }
    static class MessageListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            Message message = event.getMessage();
            String content = message.getContentRaw();
            if (!event.getAuthor().isBot()) {
                if (content.equals("!ava")) {
                    String avatarUrl = event.getAuthor().getEffectiveAvatarUrl();
                    event.getTextChannel().sendMessage(avatarUrl).submit();
                }
                if (content.startsWith("!cube")) {
                    event.getTextChannel().sendMessage(String.format(":game_die::game_die:...\n%d",
                            Math.round(Math.random() * 5 + 1))).submit();
                }
                if (content.equals("!help")) {
                    event.getTextChannel().sendMessage("**LAMBDA BOT COMMANDS**:\n !ava - your avatar\n !cube - returns random number from 1 to 6").submit();

                }
            }
            {
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
    }
}

