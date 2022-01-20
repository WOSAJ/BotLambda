package tk.wosaj.lambda.setup;

import tk.wosaj.lambda.commands.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import tk.wosaj.lambda.commands.RegisterCommand;
import tk.wosaj.lambda.commands.normal.TestCommand;

import javax.security.auth.login.LoginException;
import java.lang.annotation.Annotation;
import java.util.Objects;

public class Main {

    public static String defaultPrefix = "?";
    public static JDA bot;
    public static CommandManager manager;

    static {
        try {
            bot = JDABuilder.createDefault(System.getenv("TOKEN"))
                    .setStatus(OnlineStatus.ONLINE)
                    .setActivity(Activity.playing("/help for help"))
                    .build();
            manager = new CommandManager(bot, defaultPrefix);
            bot.addEventListener(manager);
            manager.initRegisters();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        bot.awaitReady();
    }
}
