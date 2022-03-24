package tk.wosaj.lambda.commands.custom;

import tk.wosaj.lambda.util.JSONSerializable;
import tk.wosaj.lambda.util.Strainer;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class CustomCommand implements JSONSerializable {
    @Nonnull
    private String guildId;
    @Nonnull
    private String name;
    private String group = "";
    private Strainer policy = Strainer.PUBLIC;
    private String js = "";

    public CustomCommand(@Nonnull String guildId, @Nonnull String name) {
        this.guildId = guildId;
        this.name = name;
    }

    public CustomCommand(@Nonnull String guildId, @Nonnull String name, String group) {
        this.guildId = guildId;
        this.name = name;
        this.group = group;
    }

    public CustomCommand(@Nonnull String guildId, @Nonnull String name, String group, String js) {
        this.guildId = guildId;
        this.name = name;
        this.group = group;
        this.js = js;
    }

    @Nonnull
    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(@Nonnull String guildId) {
        this.guildId = guildId;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Strainer getPolicy() {
        return policy;
    }

    public void setPolicy(Strainer policy) {
        this.policy = policy;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }
}
