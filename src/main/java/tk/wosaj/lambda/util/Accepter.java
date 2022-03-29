package tk.wosaj.lambda.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public enum Accepter {
    ADMIN,
    MODERATOR,
    ALL;

    public boolean check(Member member) {
        switch(this) {
            case ADMIN: return member.hasPermission(Permission.ADMINISTRATOR);
            case MODERATOR:
                List<String> roles = GuildDataSettings.load(member.getGuild().getId()).moderatorRolesIDs;
                for (String role : member.getRoles().stream().map(Role::getId).collect(Collectors.toList())) {
                    if(roles.contains(role)) return true;
                }
                break;
            case ALL: return true;
        }
        return false;
    }
}
