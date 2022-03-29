package tk.wosaj.lambda.util;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.database.guild.GuildService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static tk.wosaj.lambda.Main.bot;

public class GuildDataSettings implements JSONSerializable {
    @Nullable
    public transient JDA jda;
    public String prefix = Main.defaultPrefix;
    public String techChannel = "-1";
    public final String guild;
    public final List<String> blacklistCommands = new ArrayList<>();
    public final List<String> mutedUserIDs = new ArrayList<>();
    @Deprecated
    public final List<String> permittedUserIDs = new ArrayList<>();
    public final List<String> moderatorRolesIDs = new ArrayList<>();
    public final List<String> customCommands = new ArrayList<>();

    public GuildDataSettings(@Nonnull Guild guild) {
        this.guild = guild.getId();
        this.jda = guild.getJDA();
    }

    @Nonnull
    public static GuildDataSettings loadFromJson(@Nonnull String json) {
        GuildDataSettings settings = new Gson().fromJson(json, GuildDataSettings.class);
        settings.jda = bot;
        return settings;
    }

    @Nonnull
    public static GuildDataSettings load(@Nonnull String guildId) {
        GuildService service = new GuildService();
        return loadFromJson(service.byId(guildId).getJson());
    }
}
