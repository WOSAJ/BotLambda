package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return isOptional == argument.isOptional && type == argument.type && name.equals(argument.name) && description.equals(argument.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, description, isOptional);
    }

    @Override
    @Nonnull
    public String toString() {
        return "Argument{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }
}
