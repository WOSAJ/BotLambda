package tk.wosaj.lambda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import tk.wosaj.lambda.util.JSONSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandSettings implements JSONSerializable {

    private transient final JDA jda;
    private final String guild;
    private final Map<String, List<CommandProperty>> propertiesMap = new HashMap<>();

    public CommandSettings(Guild guild) {
        this.jda = guild.getJDA();
        this.guild = guild.getId();
    }

    public CommandSettings(String guildId, JDA jda) {
        this.guild = guildId;
        this.jda = jda;
    }

    public Guild getGuild() {
        return jda.getGuildById(guild);
    }

    public void addProperty(CommandProperty property) {
        for (List<CommandProperty> value : propertiesMap.values()) {
            if(value.contains(property)) return;
        }
        if (!propertiesMap.containsKey(property.getGroup())) {
            propertiesMap.put(property.getGroup(), new ArrayList<>());
        }
        propertiesMap.get(property.getGroup()).add(property);
    }

    public void removeProperty(CommandProperty property) {
        if(propertiesMap.containsKey(property.getGroup())) return;
        for (List<CommandProperty> value : propertiesMap.values()) {
            if(!value.contains(property)) return;
        }
        propertiesMap.remove(property.getGroup());
    }
}
