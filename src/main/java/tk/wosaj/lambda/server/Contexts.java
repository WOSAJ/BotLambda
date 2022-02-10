package tk.wosaj.lambda.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Contexts {
    public static final List<Context> contexts = new ArrayList<>();
    private static boolean initialized;
    public static void init() {
        Reflections reflections = new Reflections();
        Set<Class<? extends Context>> classes = reflections.getSubTypesOf(Context.class);
        for (Class<?> aClass : classes) {
            try {
                 Context context = (Context) aClass.getDeclaredConstructors()[0].newInstance();
                 contexts.add(context);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        initialized = true;
    }

    public static void addContexts(HttpServer server) {
        if(!initialized) init();
         contexts.forEach(context -> {
            HttpContext httpContext = server.createContext(context.getName(), context.getHandler());
            if(context.getAuthenticator() != null) {
                httpContext.setAuthenticator(context.getAuthenticator());
            }
        });
    }
}
