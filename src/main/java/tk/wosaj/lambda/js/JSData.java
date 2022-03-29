package tk.wosaj.lambda.js;

import net.dv8tion.jda.api.entities.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class JSData {
    private JSData() {}
    public static final class JSGuild {
        public final String id;
        public final String name;
        public final String iconURL;
        public final JSUser[] users;
        public final JSMember[] members;
        public final boolean acceptToChangeIcon;
        boolean iconQuote = false;
        boolean nameQuote = false;
        final Guild guild;
        static final java.util.Map<String, Long> timeouts = new HashMap<>();

        JSGuild(@Nonnull Guild guild) {
            id = guild.getId();
            name = guild.getName();
            iconURL = guild.getIconUrl();
            users = guild.getMembers().stream().map(m -> new JSUser(m.getUser())).toArray(JSUser[]::new);
            members = guild.getMembers().stream().map(JSMember::new).toArray(JSMember[]::new);
            acceptToChangeIcon = !timeouts.containsKey(id) || ((System.currentTimeMillis() - timeouts.get(id)) > 600000);
            this.guild = guild;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JSGuild jsGuild = (JSGuild) o;
            return Objects.equals(id, jsGuild.id) && Objects.equals(name, jsGuild.name) && Objects.equals(iconURL, jsGuild.iconURL) && Arrays.equals(users, jsGuild.users) && Arrays.equals(members, jsGuild.members) && guild.equals(jsGuild.guild);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(id, name, iconURL, guild);
            result = 31 * result + Arrays.hashCode(users);
            result = 31 * result + Arrays.hashCode(members);
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "JSGuild{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", iconURL='" + iconURL + '\'' +
                    ", users=" + Arrays.toString(users) +
                    ", members=" + Arrays.toString(members) +
                    '}';
        }

        public void leave() {
            guild.leave().queue();
        }

        public void setName(String name) {
            if(!nameQuote) guild.getManager().setName(name).queue();
            else throw new RuntimeException("Guild name change quote");
            nameQuote = true;
        }

        public void setIcon(String avatarURL) throws Exception {
            if(timeouts.containsKey(id)) {
                if((System.currentTimeMillis() - timeouts.get(id)) > 600000) timeouts.remove(id);
                else return;
            }
            timeouts.put(id, System.currentTimeMillis());
            if(!iconQuote) {
                OkHttpClient build = new OkHttpClient.Builder().build();
                guild.getManager().setIcon(Icon.from(Objects.requireNonNull(build.newCall(new Request.Builder().url(avatarURL).build())
                        .execute().body()).byteStream())).queue();
            } else throw new RuntimeException("Guild name change quote");
            iconQuote = true;
        }
    }

    public static final class JSUser {
        public final String id;
        public final String name;
        public final String avatarURL;
        public final boolean bot;
        public final String ping;
        final User user;
        boolean dmed = false;

        JSUser(@Nonnull User user) {
            id = user.getId();
            name = user.getName();
            avatarURL = user.getAvatarUrl();
            bot = user.isBot();
            ping = user.getAsMention();
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JSUser jsUser = (JSUser) o;
            return bot == jsUser.bot && Objects.equals(id, jsUser.id) && Objects.equals(name, jsUser.name) && Objects.equals(avatarURL, jsUser.avatarURL) && Objects.equals(ping, jsUser.ping) && user.equals(jsUser.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, avatarURL, bot, ping, user);
        }

        @Nonnull
        @Override
        public String toString() {
            return "JSUser{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", avatarURL='" + avatarURL + '\'' +
                    ", bot=" + bot +
                    ", ping='" + ping + '\'' +
                    '}';

        }

        public void DM(String message) {
            if (!dmed) {
                user.openPrivateChannel().queue(privateChannel ->
                        privateChannel.sendMessage(message).queue(t -> {}, t -> {}), t -> {});
            }
            dmed = true;
        }
    }

    public static final class JSRole {
        public final String id;
        public final String name;
        public final String[] permissions;
        public final String ping;
        public final String color;
        final Role role;

        JSRole(@Nonnull Role role) {
            id = role.getId();
            name = role.getName();
            permissions = role.getPermissions().stream().map(p -> p.getName().toUpperCase()).toArray(String[]::new);
            ping = role.getAsMention();
            color = String.valueOf(role.getColorRaw());
            this.role = role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JSRole jsRole = (JSRole) o;
            return Objects.equals(id, jsRole.id) && Objects.equals(name, jsRole.name) && Arrays.equals(permissions, jsRole.permissions) && Objects.equals(ping, jsRole.ping) && role.equals(jsRole.role) && color.equals(jsRole.color);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(id, name, ping, role, color);
            result = 31 * result + Arrays.hashCode(permissions);
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "JSRole{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", permissions=" + Arrays.toString(permissions) +
                    ", ping='" + ping + '\'' +
                    ", color='" + color +'\'' +
                    '}';
        }
    }

    public static final class JSMember {
        public final JSUser user;
        public final String[] permissions;
        public final JSRole[] roles;
        public final String nick;
        final Member member;

        JSMember(@Nonnull Member member) {
            user = new JSUser(member.getUser());
            permissions = member.getPermissions().stream().map(p -> p.getName().toUpperCase()).toArray(String[]::new);
            roles = member.getRoles().stream().map(JSRole::new).toArray(JSRole[]::new);
            nick = member.getNickname();
            this.member = member;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JSMember jsMember = (JSMember) o;
            return Objects.equals(user, jsMember.user) && Arrays.equals(permissions, jsMember.permissions) && Arrays.equals(roles, jsMember.roles) && Objects.equals(nick, jsMember.nick) && member.equals(jsMember.member);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(user, nick, member);
            result = 31 * result + Arrays.hashCode(permissions);
            result = 31 * result + Arrays.hashCode(roles);
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "JSMember{" +
                    "user=" + user +
                    ", permissions=" + Arrays.toString(permissions) +
                    ", roles=" + Arrays.toString(roles) +
                    ", nick='" + nick + '\'' +
                    '}';
        }

        public void ban() {
            member.ban(0).queue(t -> {}, t -> {});
        }

        public void ban(String reason) {
            member.ban(0).reason(reason).queue(t -> {}, t -> {});
        }

        public void kick() {
            member.kick().queue();
        }

        public void kick(String reason) {
            member.kick(reason).queue();
        }

        public void timeout(int days, int hours, int minutes, String reason) {
            member.timeoutFor(Duration.ofDays(days).plus(Duration.ofHours(hours)).plus(Duration.ofMinutes(minutes))).reason(reason).queue();
        }

        public void timeout(int days, int hours, int minutes) {
            timeout(days, hours, minutes, "");
        }

        public void pardon() {
            if(member.isTimedOut()) member.removeTimeout().queue();
        }
    }
}
