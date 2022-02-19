package tk.wosaj.lambda.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpHandler;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class Context {
    private String name;
    private HttpHandler handler;

    public Context(String name, HttpHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HttpHandler getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Nullable
    public abstract Authenticator getAuthenticator();
}
