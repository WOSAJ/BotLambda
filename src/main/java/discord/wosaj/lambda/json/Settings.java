package discord.wosaj.lambda.json;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("all")
public class Settings implements ISettings {
    private String prefix;
    @SerializedName("about")
    private String aboutMessage;
    private final String guildId;
    private String help;

    public Settings(String prefix, String aboutMessage, String guildId, String help) {
        this.prefix = prefix;
        this.aboutMessage = aboutMessage;
        this.guildId = guildId;
        this.help = help;
    }

    public Settings(String guildId) {
        this.guildId = guildId;
        this.aboutMessage = "";
        this.prefix = "$";
        this.help = "*No help*";
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getAboutMessage() {
        return aboutMessage;
    }

    @Override
    public void setAboutMessage(String aboutMessage) {
        this.aboutMessage = aboutMessage;
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public void setHelp(String help) {
        this.help = help;
    }
}
