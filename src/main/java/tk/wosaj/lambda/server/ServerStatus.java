package tk.wosaj.lambda.server;

import java.io.Serializable;

public enum ServerStatus implements Serializable {
    ONLINE,
    LOADING,
    WAITING,
    BLOCKED
}
