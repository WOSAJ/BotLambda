package tk.wosaj.lambda.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

public class CommandProperty implements Serializable {
    @Nonnull
    private final String group;
    @Nonnull
    private final String key;
    @Nullable
    private String value;

    public CommandProperty(@Nonnull Class<? extends Command> commandClass,
                           @Nonnull  String key,
                           @Nullable String defaultValue) {
        this.group = commandClass.getName();
        this.key = key;
        this.value = defaultValue;
    }

    public CommandProperty(@Nonnull Class<? extends Command> commandClass,
                           @Nonnull  String key) {
        this(commandClass, key, null);
    }

    @Nonnull
    public String getGroup() {
        return group;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandProperty that = (CommandProperty) o;
        return group.equals(that.group) && key.equals(that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, key, value);
    }

    @Override
    @Nonnull
    public String toString() {
        return "CommandProperty{" +
                "group='" + group + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
