package tk.wosaj.lambda.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.CommandSettings;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;

public class GuildDataSettings implements Serializable {
    public CommandSettings commandSettings;
    public String techChannel = "-1";
    public final transient Guild guild;
    public final transient ArrayList<Command> blacklistCommands = new ArrayList<>();
    public final transient ArrayList<String> mutedUserIDs = new ArrayList<>();
    public final transient ArrayList<String> permittedUserIDs = new ArrayList<>();
    public final transient ArrayList<String> moderatorRolesIDs = new ArrayList<>();

    public GuildDataSettings(Guild guild) {
        this.guild = guild;
        this.commandSettings = new CommandSettings(guild);
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

    public Guild getGuild() {
        return guild;
    }
}
