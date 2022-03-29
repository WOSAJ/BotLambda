package tk.wosaj.lambda.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

@SuppressWarnings("unused")
public abstract class Context {
    private String name;
    private HttpHandler handler;
    protected Authenticator authenticator;

    @Deprecated
    public Context(String name, HttpHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    public Context(String name) {
        this.name = name;
    }

    public void init() {
        handler = this::onCall;
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

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    protected abstract void onCall(HttpExchange exchange) throws IOException;
}
