package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.wosaj.lambda.util.JSONSerializable;
import tk.wosaj.lambda.util.Strainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class Command implements JSONSerializable {
    protected final String name;
    protected final String description;
    protected final List<Argument> arguments = new ArrayList<>();
    protected final List<CommandProperty> properties = new ArrayList<>();
    protected final boolean isNormal = true, isSlash = true, byDefault = true, ephemeral;
    protected final Strainer policy;
    protected boolean gc;

    public Command(String name, String description, boolean ephemeral, Strainer policy, Argument... arguments) {
        this.name = name;
        this.description = description;
        this.ephemeral = ephemeral;
        this.policy = policy;
        if (arguments != null) this.arguments.addAll(Arrays.stream(arguments).collect(Collectors.toList()));
    }

    public Command(String name, String description, boolean ephemeral, Argument... arguments) {
        this(name, description, ephemeral, Strainer.PUBLIC, arguments);
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

    public List<CommandProperty> getProperties() {
        return properties;
    }

    public void initProperties(@Nonnull CommandSettings target) {
       properties.forEach(target::addProperty);
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public Strainer getPolicy() {
        return policy;
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
