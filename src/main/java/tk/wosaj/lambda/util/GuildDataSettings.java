package tk.wosaj.lambda.util;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.CommandSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static tk.wosaj.lambda.Main.bot;

public class GuildDataSettings implements JSONSerializable {
    @Nullable
    public transient JDA jda;
    public String techChannel = "-1";
    public final String guild;
    public final ArrayList<String> blacklistCommands = new ArrayList<>();
    public final ArrayList<String> mutedUserIDs = new ArrayList<>();
    public final ArrayList<String> permittedUserIDs = new ArrayList<>();
    public final ArrayList<String> moderatorRolesIDs = new ArrayList<>();
    public final CommandSettings commandSettings;

    public GuildDataSettings(@Nonnull Guild guild) {
        this.guild = guild.getId();
        this.jda = guild.getJDA();
        commandSettings = new CommandSettings(guild);
    }

    public boolean isBlackListed(Command command) {
        return blacklistCommands.contains(command);
    }

    public boolean isMuted(@Nonnull User user) {
        return mutedUserIDs.contains(user.getId());
    }

    public boolean isPermitted(@Nonnull User user) {
        return permittedUserIDs.contains(user.getId());
    }

    public boolean isModerator(@Nonnull Role role) {
        return moderatorRolesIDs.contains(role.getId());
    }

    @Nonnull
    public static GuildDataSettings loadFromJson(@Nonnull String json) {
        GuildDataSettings settings = new Gson().fromJson(json, GuildDataSettings.class);
        settings.jda = bot;
        return settings;
    }
}
