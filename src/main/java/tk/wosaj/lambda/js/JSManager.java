package tk.wosaj.lambda.js;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import tk.wosaj.lambda.Main;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.util.Objects;

@SuppressWarnings("unused")
public class JSManager extends ListenerAdapter {
    public void execute(String js, Message message) {
        Context context = Context.enter();
        ScriptableObject scope = context.initStandardObjects();

        //Libs
        ScriptableObject.putConstProperty(scope, "JSC", new JSCommandService(message));
        ScriptableObject.putConstProperty(scope, "guild", new JSData.JSGuild(message.getGuild()));
        ScriptableObject.putConstProperty(scope, "bot", new JSData.JSUser(Main.bot.getSelfUser()));
        ScriptableObject.putConstProperty(scope, "embed", new JSEmbed());


        try {
            load(context, scope);
            context.evaluateString(scope, js, null, 1, null);
            context.close();
        } catch (Exception e) {
            message.reply("Exception in your code!\n" + e.getMessage()).queue();
        }
    }

    public void execute(String js, SlashCommandInteractionEvent event, boolean ephemeral) {
        Context context = Context.enter();

        //Libs
        ScriptableObject scope = context.initStandardObjects();
        ScriptableObject.putConstProperty(scope, "JSC", new JSCommandService(event, ephemeral));
        ScriptableObject.putConstProperty(scope, "guild", new JSData.JSGuild(
                Objects.requireNonNull(event.getGuild())));
        ScriptableObject.putConstProperty(scope, "embed", new JSEmbed());

        try {
            load(context, scope);
            context.evaluateString(scope, js, null, 1, null);
            context.close();
        } catch (Exception e) {
            event.getHook().sendMessage("Exception in your code!\n" + e.getMessage())
                    .setEphemeral(true).queue();
        }
    }

    private void load(@Nonnull Context context, @Nonnull Scriptable scope) throws Exception {
        context.evaluateReader(scope, new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("js/global.js")
                )), null, 1, null);
    }
}
