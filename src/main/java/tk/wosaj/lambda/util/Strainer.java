package tk.wosaj.lambda.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;

public class Strainer {
    private Strainer() {}

    public static boolean admin(@Nonnull Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }
}
