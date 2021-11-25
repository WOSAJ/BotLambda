package discord.wosaj.lambda.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.annotation.Nonnull;

public abstract class Command {
    private final String name;
    private final boolean isSlash;
    private final boolean hasArguments;
    private final String description;
    private final Option[] options;

    public Command(String name, boolean isSlash, boolean hasArguments, String description) {
        this.name = name;
        this.isSlash = isSlash;
        this.hasArguments = hasArguments;
        this.description = description;
        this.options = null;
    }

    public Command(String name, boolean isSlash, boolean hasArguments, String description, Option... options) {
        this.name = name;
        this.isSlash = isSlash;
        this.hasArguments = hasArguments;
        this.description = description;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public boolean isSlash() {
        return isSlash;
    }

    public boolean isHasArguments() {
        return hasArguments;
    }

    public String getDescription() {
        return description;
    }

    public Option[] getOptions() {
        return options;
    }

    public abstract void run(@Nonnull MessageReceivedEvent event);
    public abstract void runAsSlashCommand(@Nonnull SlashCommandEvent event);

    @SuppressWarnings("all")
    public static class Option {
        private final OptionType type;
        private final String name;
        private final boolean required;
        private String description;

        public Option(OptionType type, String name, String description, boolean required) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.required = required;
        }

        public OptionType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isRequired() {
            return required;
        }
    }
}
