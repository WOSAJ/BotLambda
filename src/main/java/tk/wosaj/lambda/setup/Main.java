package tk.wosaj.lambda.setup;

import com.sun.net.httpserver.HttpServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.CommandManager;
import tk.wosaj.lambda.commands.RegisterCommand;
import tk.wosaj.lambda.server.Contexts;
import tk.wosaj.lambda.server.ServerStatus;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public final class Main {
    public static JDA bot;
    public static CommandManager manager;
    public static HttpServer server;
    public static ServerStatus status = ServerStatus.LOADING;
    public static final String defaultPrefix = "?";
    public static final Properties serverProperties = new Properties();

    static {
        System.out.println(System.getenv("JDBC_DATABASE_URL"));
        try {
            serverProperties.load(Main.class.getClassLoader().getResourceAsStream("properties/server.properties"));
            server = HttpServer.create(
                    new InetSocketAddress(
                            serverProperties.getProperty("ip"),
                            Integer.parseInt(System.getenv("PORT"))), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread botThread = new Thread(() -> {
            try {
                pingAnother();
                bot = JDABuilder.createDefault(System.getenv("TOKEN"))
                        .setStatus(OnlineStatus.ONLINE)
                        .setActivity(Activity.playing("/help for help"))
                        .build();
                manager = new CommandManager(bot, defaultPrefix);
                bot.addEventListener(manager);
                bot.awaitReady();
                status = ServerStatus.ONLINE;
                updateCommands();
            } catch (InterruptedException | LoginException e) {
                e.printStackTrace();
            }
        }, "Client Thread");
        Thread serverThread = new Thread(() -> {
            Contexts.addContexts(server);
            server.start();
        }, "Server Thread");
        serverThread.start();
        botThread.start();
    }

    //REFACTOR
    public static void updateCommands() {
        bot.getGuilds().forEach(guild -> guild.retrieveCommands().queue(commands -> commands.forEach(command -> {
            for (Command annotatedCommand : CommandManager.getAnnotatedCommands()) {
                if(annotatedCommand.getName().equals(command.getName())) continue;
                if(annotatedCommand.getClass().getAnnotation(RegisterCommand.class).byDefault())
                    manager.registerCommand(annotatedCommand, guild);
            }
        })));
    }

    @SuppressWarnings("BusyWait")
    private static void pingAnother() {
        try {
            String url, url1, url2;
            int host = Integer.parseInt(System.getenv("HOSTNUM"));
            url1 = serverProperties.getProperty("url1");
            url2 = serverProperties.getProperty("url2");
            if(host == 1) url = url2;
            else url = url1;
            OkHttpClient client =  new OkHttpClient.Builder().build();
            while(true) {
                try (ResponseBody body = client.newCall(new Request.Builder().url(url).build()).execute().body()) {
                    assert body != null;
                    BufferedReader reader = new BufferedReader(body.charStream());
                    StringBuilder builder = new StringBuilder();
                    String temp;
                    while ((temp = reader.readLine()) != null) builder.append(temp);
                    if(builder.toString().equals("\"ONLINE\"")) {
                        //REFACTOR
                    } else break;
                }
                System.out.println("WAIT");
                status = ServerStatus.WAITING;
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {}
        System.out.println("PASS");
    }
}
