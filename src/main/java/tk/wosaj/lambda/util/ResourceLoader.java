package tk.wosaj.lambda.util;

import java.io.Reader;
import java.net.URL;

public interface ResourceLoader {
    String loadString(String name);
    URL loadURI(String name);
    Reader getReader(String name);
}
