package tk.wosaj.lambda.server.contexts;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.server.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("all")
public final class StatusContext extends Context {
    public StatusContext() {
        super("/ping");
    }

    @Override
    public Authenticator getAuthenticator() {
        return null;
    }

    @Override
    protected void onCall(HttpExchange httpExchange) throws IOException {
        try(OutputStream outputStream = httpExchange.getResponseBody()) {
            String resp = String.format("\"%s\"", Main.status.toString());
            httpExchange.sendResponseHeaders(400, resp.getBytes(StandardCharsets.UTF_8).length);
            outputStream.write(resp.getBytes(StandardCharsets.UTF_8));
        } finally {
            httpExchange.close();
        }

    }
}
