package tk.wosaj.lambda.server;

import com.sun.net.httpserver.BasicAuthenticator;

@SuppressWarnings("unused")
public class CentralAuth extends BasicAuthenticator {
    public static final CentralAuth auth = new CentralAuth();
    private CentralAuth() {
        super("centralRealm");
    }

    @Override
    public boolean checkCredentials(String user, String password) {
        String[] split1 = System.getenv("PASSWORDS").split(" \\| ");
        for (String s : split1) {
            String[] split2 = s.split(" ");
            if(user.equals(split2[0]) && password.equals(split2[1])) return true;
        }
        return false;
    }
    public static class AdminAuth extends BasicAuthenticator {
        public static final AdminAuth auth = new AdminAuth();
        public AdminAuth() {
            super("adminRealm");
        }

        @Override
        public boolean checkCredentials(String user, String password) {
            String[] split = System.getenv("ADMINPASSWORD").split(" ");
            return user.equals(split[0]) && password.equals(split[1]);
        }
    }
}
