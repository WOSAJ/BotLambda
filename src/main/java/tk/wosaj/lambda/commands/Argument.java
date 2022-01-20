package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public final class Argument {
    private final OptionType type;
    private final String name, description;
    private final boolean isOptional;

    public Argument(OptionType type, String name, String description, boolean isOptional) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.isOptional = isOptional;
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

    public boolean isOptional() {
        return isOptional;
    }
}
