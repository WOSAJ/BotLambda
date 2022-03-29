package tk.wosaj.lambda.server.contexts;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.server.CentralAuth;
import tk.wosaj.lambda.server.Context;

import java.io.IOException;

@SuppressWarnings("unused")
public final class StopContext extends Context {
    public StopContext() {
        super("/stop");
    }

    @Override
    public Authenticator getAuthenticator() {
        return CentralAuth.AdminAuth.auth;
    }

    @Override
    protected void onCall(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
        Main.stop();
    }
}
