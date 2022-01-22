package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Commands {
    public static void reply(Message message, String text) {
        message.reply(new MessageBuilder().append(text).build()).queue();
    }

    public static void reply(Message message, Message reply) {
        message.reply(reply).queue();
    }
}
