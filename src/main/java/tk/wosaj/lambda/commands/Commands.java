package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

public class Commands {
    public static void reply(@Nonnull Message message, @Nonnull String text) {
        message.reply(new MessageBuilder().append(text).build()).queue();
    }

    public static void reply(@Nonnull Message message, @Nonnull Message reply) {
        message.reply(reply).queue();
    }
}
