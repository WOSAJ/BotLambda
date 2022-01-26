package tk.wosaj.lambda.database;

import java.util.List;

public interface IGuildDAO {
    GuildItem get(String name);
    void save(GuildItem item);
    void update(GuildItem item);
    void delete(GuildItem item);
    List<GuildItem> getAll();
}
