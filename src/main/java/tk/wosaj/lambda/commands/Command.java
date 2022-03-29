package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.wosaj.lambda.util.Accepter;
import tk.wosaj.lambda.util.JSONSerializable;
import tk.wosaj.lambda.util.Strainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Command implements JSONSerializable {
    protected final String name;
    protected final String description;
    protected final List<Argument> arguments = new ArrayList<>();
    protected final boolean isNormal = true, isSlash = true, byDefault = true;
    protected boolean ephemeral;
    @Deprecated protected Strainer policy;
    protected Accepter accepter;
    protected boolean gc;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public boolean isSlash() {
        return isSlash;
    }

    public boolean byDefault() {
        return byDefault;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    @Deprecated
    public Strainer getPolicy() {
        return policy;
    }

    public Accepter getAccepter() {
        return accepter;
    }

    public abstract void execute(@Nonnull MessageReceivedEvent event);
    public abstract void executeAsSlash(@Nonnull SlashCommandInteractionEvent event);

    @Nonnull
    public static String[] splitMessage(@Nonnull String message) {
        return message.split(" ");
    }

    protected void reply(@Nonnull SlashCommandInteractionEvent event, String content) {
        if (content == null) return;
        event.getHook().sendMessage(content).setEphemeral(ephemeral).queue();
    }

    public static void reply(@Nonnull SlashCommandInteractionEvent event, String content, boolean ephemeral) {
        if (content == null) return;
        event.getHook().sendMessage(content).setEphemeral(ephemeral).queue();
    }

    public static void reply(@Nonnull MessageReceivedEvent event, String content) {
        if (content == null) return;
        event.getMessage().reply(content).queue();
    }

    public boolean requireGC() {
        return gc;
    }
}
