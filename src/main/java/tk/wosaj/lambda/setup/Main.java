package tk.wosaj.lambda.setup;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.CommandManager;
import tk.wosaj.lambda.commands.RegisterCommand;

import javax.security.auth.login.LoginException;

public final class Main {

    public static String defaultPrefix = "?";
    public static JDA bot;
    public static CommandManager manager;

    static {
        System.out.println(System.getenv("JDBC_DATABASE_URL"));
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
        updateCommands();
    }

    static void updateCommands() {
        bot.getGuilds().forEach(guild -> guild.retrieveCommands().queue(commands -> commands.forEach(command -> {
            for (Command annotatedCommand : CommandManager.getAnnotatedCommands()) {
                if(annotatedCommand.getName().equals(command.getName())) continue;
                if(annotatedCommand.getClass().getAnnotation(RegisterCommand.class).byDefault())
                    manager.registerCommand(annotatedCommand, guild);
            }
        })));
    }
}
