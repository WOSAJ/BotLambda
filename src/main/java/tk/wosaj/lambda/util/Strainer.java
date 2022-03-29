package tk.wosaj.lambda.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import tk.wosaj.lambda.database.guild.GuildItem;
import tk.wosaj.lambda.database.guild.GuildService;

import javax.annotation.Nonnull;
import java.util.List;

@Deprecated
public enum Strainer {
    ADMIN,
    MODERATOR,
    COMPLEX_ADMIN,
    COMPLEX_MODERATOR,
    PUBLIC;
    public static boolean admin(@Nonnull Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    public static boolean permitted(@Nonnull Member member) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(member.getGuild().getId());
        return GuildDataSettings.loadFromJson(item.getJson()).permittedUserIDs.contains(member.getUser().getId());
    }

    public static boolean moderator(@Nonnull Member member) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(member.getGuild().getId());
        List<String> moderatorRolesIDs = GuildDataSettings.loadFromJson(item.getJson()).moderatorRolesIDs;
        for (Role role : member.getRoles()) {
            if(moderatorRolesIDs.contains(role.getId())) return true;
        }
        return false;
    }

    public static boolean moderator(@Nonnull Role role) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(role.getGuild().getId());
        return GuildDataSettings.loadFromJson(item.getJson()).moderatorRolesIDs.contains(role.getId());
    }

    public static boolean complexAdmin(@Nonnull Member member) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(member.getGuild().getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        return member.hasPermission(Permission.ADMINISTRATOR)
                || settings.permittedUserIDs.contains(member.getUser().getId());
    }

    public static boolean complexModerator(@Nonnull Member member) {
        GuildService service = new GuildService();
        GuildItem item = service.byId(member.getGuild().getId());
        GuildDataSettings settings = GuildDataSettings.loadFromJson(item.getJson());
        boolean moder = false;
        for (Role role : member.getRoles()) {
            if(settings.moderatorRolesIDs.contains(role.getId())) {
                moder = true;
                break;
            }
        }
        return moder
            || member.hasPermission(Permission.ADMINISTRATOR)
            || settings.permittedUserIDs.contains(member.getUser().getId());
    }
}
