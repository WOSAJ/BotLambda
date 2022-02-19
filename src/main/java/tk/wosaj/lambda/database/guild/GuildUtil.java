package tk.wosaj.lambda.database.guild;

import tk.wosaj.lambda.Main;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Properties;

public class GuildUtil {
    private GuildUtil() {}
    @Nonnull
    public static String generateDatabaseName(@Nonnull String guildId) {
        String prefix;
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getClassLoader().getResourceAsStream("properties/data.properties"));
            prefix = properties.getProperty("guild.prefix");
            return prefix + guildId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Properties not found");
    }
}
