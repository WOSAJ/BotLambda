package tk.wosaj.lambda.setup;

import tk.wosaj.lambda.database.GuildItem;
import tk.wosaj.lambda.database.GuildService;

public class DatabaseTestSetup {
    public static void main(String[] args) {
        GuildService service = new GuildService();
        GuildItem item = new GuildItem("test", "");
        service.save(item);
    }
}
