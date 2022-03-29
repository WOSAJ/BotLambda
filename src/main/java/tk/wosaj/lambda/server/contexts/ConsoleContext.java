package tk.wosaj.lambda.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.server.CentralAuth;
import tk.wosaj.lambda.server.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public final class ConsoleContext extends Context {
    public ConsoleContext() {
        super("/console");
        authenticator = CentralAuth.AdminAuth.auth;
    }

    @Override
    protected void onCall(HttpExchange exchange) throws IOException {
        final String out = Main.consoleCollector;
        if(!Main.canReadCollector) {
            exchange.sendResponseHeaders(505, 0);
            return;
        }
        exchange.sendResponseHeaders(200, out.length());
        exchange.getResponseBody().write(out.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
        exchange.close();
    }
}
