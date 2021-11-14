package discord.wosaj.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings("all")
    public synchronized String createAttribute(@Nonnull AttributeContainer container, @Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");
        File directory = path.toAbsolutePath().toFile();

        if(!directory.exists()) directory.mkdirs();
        File file = filePath.toFile();

        if(!file.exists()) file.createNewFile();
        Writer writer = new FileWriter(file);

        String out = gson.toJson(container);

        writer.write(out);
        writer.flush();
        writer.close();
        return out;
    }

    @SuppressWarnings("all")
    @Nullable
    public synchronized String addAttribute(@Nonnull Attribute<?> attribute, @Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { directory.mkdirs(); return null; }

        File file = filePath.toFile();
        if(!file.exists()) { file.createNewFile(); return null; }

        BufferedReader reader = new BufferedReader(new FileReader(file));


        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        AttributeContainer inputContainer = gson.fromJson(input, AttributeContainer.class);
        inputContainer.add(attribute);
        String output = gson.toJson(inputContainer);
        Writer writer = new FileWriter(file);
        writer.write(output);
        writer.flush();
        writer.close();
        return output;
    }

    @SuppressWarnings("all")
    @Nullable
    public synchronized String removeAttribute(@Nonnull String name, @Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { directory.mkdirs(); return null; }

        File file = filePath.toFile();
        if(!file.exists()) { file.createNewFile(); return null; }

       BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

       while ((current = reader.readLine()) != null) {
           input += current;
           input += "\n";
       }
       reader.close();
        AttributeContainer inputContainer = gson.fromJson(input, AttributeContainer.class);
        if (!inputContainer.getAttributes().containsKey(name)) return null;
        inputContainer.remove(name);
        String output = gson.toJson(inputContainer);
        Writer writer = new FileWriter(file);
        writer.write(output);
        writer.flush();
        writer.close();
        return output;
    }

    @Nullable
    @SuppressWarnings("all")
    public Attribute<?> getAttribute(@Nonnull String name, @Nonnull String guildId) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { directory.mkdirs(); return null; }

        File file = filePath.toFile();
        if(!file.exists()) { file.createNewFile(); return null; }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        AttributeContainer inputContainer = gson.fromJson(input, AttributeContainer.class);
        if (!inputContainer.getAttributes().containsKey(name)) return null;
        return inputContainer.getAttributes().get(name);
    }

    @Nullable
    @SuppressWarnings("all")
    public synchronized String setAttribute(@Nonnull String name, @Nonnull String guildId, @Nonnull Object value) throws IOException {
        Path path = Paths.get("./data/" + guildId);
        Path filePath = Paths.get("./data/" + guildId + "/attributes.json");

        File directory = path.toAbsolutePath().toFile();
        if(!directory.exists()) { directory.mkdirs(); return null; }

        File file = filePath.toFile();
        if(!file.exists()) { file.createNewFile(); return null; }

        BufferedReader reader = new BufferedReader(new FileReader(file));


        String current;
        String input = "";

        while ((current = reader.readLine()) != null) {
            input += current;
            input += "\n";
        }
        reader.close();
        AttributeContainer inputContainer = gson.fromJson(input, AttributeContainer.class);
        if (!inputContainer.getAttributes().containsKey(name)) return null;

        inputContainer.remove(name);
        inputContainer.add(new Attribute<>(name, value));
        String output = gson.toJson(inputContainer);

        Writer writer = new FileWriter(file);
        writer.write(output);
        writer.flush();
        writer.close();

        return output;
    }
}
