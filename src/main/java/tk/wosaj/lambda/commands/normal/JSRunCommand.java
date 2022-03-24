package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.util.GuildDataSettings;
import tk.wosaj.lambda.util.Strainer;

import javax.annotation.Nonnull;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unused")
public final class JSRunCommand extends Command {
    {
        gc = true;
    }
    public JSRunCommand() {
        super(
                "js",
                "Run js command script",
                false,
                Strainer.ADMIN,
                new Argument(
                        OptionType.STRING,
                        "script",
                        "JS script to run",
                        false));
    }

    @Override
    public void execute(@Nonnull MessageReceivedEvent event) {
        Thread thisThread = Thread.currentThread();
        GuildDataSettings load = GuildDataSettings.load(event.getGuild().getId());
        if(event.getMessage().getContentRaw().startsWith(load.prefix + name + ' ')) {
            String substring = event.getMessage().getContentRaw().substring(
                    (load.prefix + name + ' ').length());
            if(substring.trim().equals("")) {
                event.getMessage().reply("Empty script").queue();
                return;
            }
            Timer t = new Timer("JSInterrupter");
            try {
                t.schedule(new TimerTask() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        reply(event, "Thread was interrupted");
                        thisThread.interrupt();
                        thisThread.stop();
                    }
                }, 15000);
                Main.js.execute(substring, event.getMessage());
            } finally {
                t.cancel();
            }
        }
    }

    @Override
    public void executeAsSlash(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        OptionMapping substring_ = event.getOption("script");
        if (substring_ == null) {
            event.getHook().sendMessage("No js script").queue();
            return;
        }
        String substring = substring_.getAsString().trim();
        if (substring.equals("")) {
            event.getHook().sendMessage("No js script").queue();
            return;
        }
        Thread thisThread = Thread.currentThread();
        GuildDataSettings load = GuildDataSettings.load(event.getGuild().getId());
        Timer t = new Timer("JSInterrupter");
        try {
            t.schedule(new TimerTask() {
                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    reply(event, "Thread was interrupted");
                    thisThread.interrupt();
                    thisThread.stop();
                }
            }, 15000);
            Main.js.execute(substring, event, ephemeral);
        } finally {
            t.cancel();
        }
    }
}
