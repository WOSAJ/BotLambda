package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

@SuppressWarnings("all")
public interface Command {
    default boolean isSlash() {
        return true;
    }
    default boolean isNormal() {
        return true;
    }
    boolean hasArguments();
    Argument[] getArguments();
    void execute(MessageReceivedEvent event);
    void executeAsSlash(SlashCommandEvent event);
    @Nonnull
    String getName();
    @Nonnull
    String getDescription();
}
