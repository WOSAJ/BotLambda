package tk.wosaj.lambda.server.contexts;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.server.CentralAuth;
import tk.wosaj.lambda.server.Context;
import tk.wosaj.lambda.server.ServerStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public final class BlockContext extends Context {
    public BlockContext() {
        super("/block");
    }

    @Override
    public Authenticator getAuthenticator() {
        return CentralAuth.AdminAuth.auth;
    }

    @Override
    protected void onCall(HttpExchange httpExchange) throws IOException {

        if(Main.status == ServerStatus.BLOCKED) {
            String response = "Unblocked";
            Main.status = ServerStatus.ONLINE;
            httpExchange.sendResponseHeaders(400, response.length());
            httpExchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } else if (Main.status == ServerStatus.ONLINE) {
            Main.status = ServerStatus.BLOCKED;
            String response = "Blocked";
            httpExchange.sendResponseHeaders(400, response.length());
            httpExchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        }
        httpExchange.getResponseBody().flush();
        httpExchange.getResponseBody().close();
        httpExchange.close();
    }
}
