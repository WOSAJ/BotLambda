package tk.wosaj.lambda.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JSONSerializable {
    default String toJson(boolean prettyPrinting) {
        Gson gson = prettyPrinting
                ? new GsonBuilder().setPrettyPrinting().serializeNulls().create()
                : new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }
}
