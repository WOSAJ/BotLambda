package discord.wosaj.lambda;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class AttributeContainer {
    private String guildId;
    private HashMap<String, Attribute<?>> attributes = new HashMap<>();

    public AttributeContainer(@Nonnull String guildId) {
        this.guildId = guildId;
    }

    public AttributeContainer(@Nonnull String guildId, @Nonnull  Attribute<?>... attribute) {
        this.guildId = guildId;
        for(Attribute<?> attribute1 : attribute) {
            attributes.put(attribute1.getName(), attribute1);
        }
    }

    public void add(@Nonnull Attribute<?>... attribute) {
        for (Attribute<?> attribute1 : attribute) {
            attributes.put(attribute1.getName(), attribute1);
        }
    }
    public void remove(@Nonnull String name) {
        attributes.remove(name);
    }
    public HashMap<String, Attribute<?>> getAttributes() {
        return attributes;
    }
}
