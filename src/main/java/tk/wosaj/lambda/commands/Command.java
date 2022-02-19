package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command {
    protected final String name;
    protected final String description;
    protected final List<Argument> arguments = new ArrayList<>();
    protected final List<CommandProperty> properties = new ArrayList<>();
    protected final boolean isNormal = true, isSlash = true, byDefault = true, ephemeral;

    public Command(String name, String description, boolean ephemeral, Argument... arguments) {
        this.name = name;
        this.description = description;
        this.ephemeral = ephemeral;
        if (arguments != null) this.arguments.addAll(Arrays.stream(arguments).collect(Collectors.toList()));
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

    protected void reply(@Nonnull SlashCommandEvent event, String content) {
        if (content == null) return;
        event.getHook().sendMessage(content).setEphemeral(ephemeral).queue();
    }

    protected void reply(@Nonnull MessageReceivedEvent event, String content) {
        if (content == null) return;
        event.getMessage().reply(content).queue();
    }

    public abstract void execute(MessageReceivedEvent event);
    public abstract void executeAsSlash(SlashCommandEvent event);

    @Nonnull
    public static String[] splitMessage(@Nonnull String message) {
        return message.split(" ");
    }
}
