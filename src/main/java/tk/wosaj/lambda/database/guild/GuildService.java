package tk.wosaj.lambda.database.guild;

import java.util.List;

public class GuildService {
    private final GuildDAO dao = new GuildDAO();

    public GuildItem get(String name) {
        return dao.get(name);
    }

    public GuildItem byId(String guildId) {
        return get(GuildUtil.generateDatabaseName(guildId));
    }

    public synchronized void save(GuildItem item) {
        dao.save(item);
    }

    public synchronized void update(GuildItem item) {
        dao.update(item);
    }

    public synchronized void delete(GuildItem item) {
        dao.delete(item);
    }

    public List<GuildItem> getAll() {
        return dao.getAll();
    }
}
