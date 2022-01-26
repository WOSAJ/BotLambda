package tk.wosaj.lambda.database;

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
