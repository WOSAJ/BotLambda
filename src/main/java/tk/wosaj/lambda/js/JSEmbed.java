package tk.wosaj.lambda.js;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Clock;

@SuppressWarnings("unused")
@CheckReturnValue
public class JSEmbed {
    JSEmbed() {}
    public EmbedBuilder builder(JSCommandService.JSBuilder builder) {
        return new EmbedBuilder(builder);
    }

    public static final class EmbedBuilder {
        private net.dv8tion.jda.api.EmbedBuilder embedBuilder = new net.dv8tion.jda.api.EmbedBuilder();
        private final JSCommandService.JSBuilder builder;

        private EmbedBuilder(JSCommandService.JSBuilder builder) {
            this.builder = builder;
        }

        public EmbedBuilder addField(String name, String value, boolean inline) {
            embedBuilder = embedBuilder.addField(name, value, inline);
            return this;
        }

        public EmbedBuilder addField(String name, String value) {
            return addField(name, value, false);
        }

        @SuppressWarnings("all")
        public EmbedBuilder color(int r, int g, int b) {
            embedBuilder.setColor(new Color(r, g, b));
            return this;
        }

        public EmbedBuilder color(@Nonnull String htmlColor) {
            if(!htmlColor.startsWith("#")) throw new IllegalArgumentException("# expired");
            char[] chars = htmlColor.substring(1).toCharArray();
            int r = Integer.parseInt(new String(new char[]{chars[0],chars[1]}),16);
            int g = Integer.parseInt(new String(new char[]{chars[2],chars[3]}),16);
            int b = Integer.parseInt(new String(new char[]{chars[4],chars[5]}),16);
            return color(r,g,b);
        }

        public EmbedBuilder description(String content) {
            embedBuilder.setDescription(content);
            return this;
        }

        public EmbedBuilder author(String name, String url, String iconUrl) {
            embedBuilder = embedBuilder.setAuthor(name, url, iconUrl);
            return this;
        }

        public EmbedBuilder author(String name, String iconUrl) {
            return author(name, null, iconUrl);
        }

        public EmbedBuilder author(String name) {
            return author(name, null ,null);
        }

        public EmbedBuilder image(String url) {
            embedBuilder = embedBuilder.setImage(url);
            return this;
        }

        public EmbedBuilder thumbnail(String url) {
            embedBuilder = embedBuilder.setThumbnail(url);
            return this;
        }

        public EmbedBuilder timestamp() {
            embedBuilder = embedBuilder.setTimestamp(Clock.systemUTC().instant());
            return this;
        }

        public void build() {
            builder.embed(embedBuilder.build());
        }
    }
}
