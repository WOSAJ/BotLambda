package tk.wosaj.lambda.util;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class StaticResourceLoader implements ResourceLoader {
    private final Class<?> clazz;
    public StaticResourceLoader(Class<?> mainClass) {
        this.clazz = mainClass;
    }

    @Nullable
    @Override
    public String loadString(String name) {
        InputStream stream = clazz.getClassLoader().getResourceAsStream(name);
        if (stream != null)
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String temp;
            StringBuilder builder = new StringBuilder();
            while((temp = reader.readLine()) != null) builder.append(temp).append('\n');
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL loadURI(String name) {
        return clazz.getClassLoader().getResource(name);
    }

    @Override
    public Reader getReader(String name) {
        return new InputStreamReader(Objects.requireNonNull(clazz.getClassLoader().getResourceAsStream(name)));
    }

    @SuppressWarnings("unused")
    public Class<?> getMainClass() {
        return clazz;
    }
}
