package discord.wosaj.lambda.json;

public interface ISettings {
    String getPrefix();
    void setPrefix(String prefix);
    String getAboutMessage();
    void setAboutMessage(String aboutMessage);
    String getGuildId();
    void setHelp(String help);
    String getHelp();
}
