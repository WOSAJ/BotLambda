package discord.wosaj.lambda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

public class Main {

    public static final String prefix = "$";
    public static final Logger MAIN_LOGGER = Logger.getLogger("Main logs");
    static JDA jda;

    public static void main(String[] args) throws LoginException, IOException {

        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .addEventListeners(new MessageListener())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.competing(prefix + "help for help"))
                .build();

        jda.upsertCommand("help", "Sends a help page").queue();
        jda.upsertCommand("ava", "Sends your avatar").queue();
        jda.upsertCommand("cube", "Sends random number from 1 to 6").queue();

        new JsonManager().createAttribute(new AttributeContainer("903420089838223370",
                new Attribute<>("int", 4),
                new Attribute<>("string", "test")), "903420089838223370");
        new JsonManager().removeAttribute("int", "903420089838223370");
        new JsonManager().addAttribute(new Attribute<>("bool", true), "903420089838223370");
        new JsonManager().setAttribute("bool", "903420089838223370", false);

    }
    static class MessageListener extends ListenerAdapter {
        @Override
        public void onSlashCommand(@Nonnull SlashCommandEvent event) {
            switch(event.getName()) {
                case ("help"):
                    event.reply("**LAMBDA BOT COMMANDS**:\n !ava - your avatar\n !cube - returns random number from 1 to 6").setEphemeral(true).submit();
                    MAIN_LOGGER.info("Command executed: "
                            + event.getName()
                            + " with options: "
                            + event.getOptions()
                            + " by "
                            + event.getUser());
                    break;
                case ("cube"):
                    event.reply(String.format(":game_die::game_die:...\n%d",
                            Math.round(Math.random() * 5 + 1))).submit();
                    MAIN_LOGGER.info("Command executed: "
                            + event.getName()
                            + " with options: "
                            + event.getOptions()
                            + " by "
                            + event.getUser());
                    break;
                case ("ava"):
                    event.reply(event.getUser().getEffectiveAvatarUrl() + "?size=1024").submit();
                    MAIN_LOGGER.info("Command executed: "
                            + event.getName()
                            + " with options: "
                            + event.getOptions()
                            + " by "
                            + event.getUser());
                    break;
            }
        }

        @Override
        public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
            Message message = event.getMessage();
            String content = message.getContentRaw();
            if (!event.getAuthor().isBot()) {
                switch (content) {
                    case prefix + "ava":
                        String avatarUrl = event.getAuthor().getEffectiveAvatarUrl();
                        event.getTextChannel().sendMessage(avatarUrl).submit();
                        break;

                    case prefix + "cube":
                        event.getTextChannel().sendMessage(String.format(":game_die::game_die:...\n%d",
                                Math.round(Math.random() * 5 + 1))).submit();
                        MAIN_LOGGER.info("Command executed: ");
                        break;

                    case prefix + "help":
                        event.getTextChannel().sendMessage("**LAMBDA BOT COMMANDS**:\n !ava - your avatar\n !cube - returns random number from 1 to 6").submit();
                        break;
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


