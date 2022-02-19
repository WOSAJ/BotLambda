package tk.wosaj.lambda.database.guild;

import tk.wosaj.lambda.util.JSONSerializable;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Table(name = "guilds")
@Entity
public class GuildItem {
    @Id
    private String name;
    @Column(name = "json")
    private String json;
    public GuildItem() {}
    public GuildItem(String name, String json) {
        this.name = name;
        this.json = json;
    }
    public GuildItem(String name, @Nonnull JSONSerializable jsonSerializable) {
        this(name, jsonSerializable.toJson(false));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "GuildItem{" +
                "name='" + name + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
