package discord.wosaj.lambda.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import discord.wosaj.lambda.commands.Command;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public synchronized String createCommandListFile(@Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/commands.json");
        File directory = path.toAbsolutePath().toFile();

        if(!directory.exists()) directory.mkdirs();
        File file = filePath.toFile();

        if(!file.exists()) file.createNewFile();
        Writer writer = new FileWriter(file);

        String out = gson.toJson(new ArrayList<Command>());

        writer.write(out);
        writer.flush();
        writer.close();
        return out;
    }

    public List<Command> getCommandList(@Nonnull String guildId) throws IOException {
            Path path = Paths.get("./data/" + guildId);
            Path filePath = Paths.get("./data/" + guildId + "/commands.json");

            File directory = path.toAbsolutePath().toFile();
            if(!directory.exists()) { createCommandListFile(guildId); }

            File file = filePath.toFile();
            if(!file.exists()) { createCommandListFile(guildId); }

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String current;
            String input = "";

            while ((current = reader.readLine()) != null) {
                input += current;
                input += "\n";
            }
            reader.close();
            return gson.fromJson(input, List.class);
    }

    public synchronized String addCommand(@Nonnull String guildId, @Nonnull Command command) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { createCommandListFile(guildId); }

        File file = filePath.toFile();
        if(!file.exists()) { createCommandListFile(guildId); }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();

        Writer writer = new FileWriter(file);
        List<Command> inputList = gson.fromJson(input, List.class);
        inputList.add(command);
        String out = gson.toJson(inputList);

        writer.write(out);
        writer.flush();
        writer.close();

        return out;
    }



    @SuppressWarnings("all")
    public synchronized String createSettingsFile(@Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");
        File directory = path.toAbsolutePath().toFile();

        if(!directory.exists()) directory.mkdirs();
        File file = filePath.toFile();

        if(!file.exists()) file.createNewFile();
        Writer writer = new FileWriter(file);

        String out = gson.toJson(new Settings(guildId));

        writer.write(out);
        writer.flush();
        writer.close();
        return out;
    }

    @SuppressWarnings("all")
    public synchronized String createSettingsFile(@Nonnull String guildId, Settings settings) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");
        File directory = path.toAbsolutePath().toFile();

        if(!directory.exists()) directory.mkdirs();
        File file = filePath.toFile();

        if(!file.exists()) file.createNewFile();
        Writer writer = new FileWriter(file);

        String out = gson.toJson(settings);

        writer.write(out);
        writer.flush();
        writer.close();
        return out;
    }

    @SuppressWarnings("all")
    public Settings getSettings(@Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { createSettingsFile(guildId); }

        File file = filePath.toFile();
        if(!file.exists()) { createSettingsFile(guildId); }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        return gson.fromJson(input, Settings.class);
    }

    @SuppressWarnings("all")
    public synchronized String setSettings(@Nonnull Settings settings) throws IOException {
        String guildId = settings.getGuildId();
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { createSettingsFile(guildId); }

        File file = filePath.toFile();
        if(!file.exists()) { createSettingsFile(guildId); }

        Writer writer = new FileWriter(file);
        String out = gson.toJson(settings);
        writer.write(out);
        writer.flush();
        writer.close();

        return out;
    }

    @SuppressWarnings("all")
    public synchronized String setPrefix(@Nonnull String guildId, @Nonnull String prefix) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { createSettingsFile(guildId); }

        File file = filePath.toFile();
        if(!file.exists()) { createSettingsFile(guildId); }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        Settings inputSettings = gson.fromJson(input, Settings.class);
        inputSettings.setPrefix(prefix);
        String output = gson.toJson(inputSettings);

        Writer writer = new FileWriter(file);
        writer.write(output);
        writer.flush();
        writer.close();

        return output;
    }

    @SuppressWarnings("all")
    public String getPrefix(@Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/settings.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { return "$"; }

        File file = filePath.toFile();
        if(!file.exists()) { return "$"; }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        return gson.fromJson(input, Settings.class).getPrefix();
    }

    public boolean isSettingsFileExist(@Nonnull String guildId) {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");
        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) return false;
        File file = filePath.toFile();
        return file.exists();
    }
}