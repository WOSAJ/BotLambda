package tk.wosaj.lambda.server.contexts;

import com.sun.net.httpserver.Authenticator;
import tk.wosaj.lambda.Main;
import tk.wosaj.lambda.server.CentralAuth;
import tk.wosaj.lambda.server.Context;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class StopContext extends Context {
    public StopContext() {
        super("/stop", httpExchange -> Main.stop());
    }

    @Nullable
    @Override
    public Authenticator getAuthenticator() {
        return CentralAuth.AdminAuth.auth;
    }
}
