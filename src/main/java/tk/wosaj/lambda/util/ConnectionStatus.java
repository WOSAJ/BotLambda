package tk.wosaj.lambda.util;

import java.io.Serializable;

public enum ConnectionStatus implements Serializable {
    OFFLINE,
    LOAD,
    WAIT,
    ONLINE,
    BLOCKED
}
