package tk.wosaj.lambda.database.guild;

import javax.annotation.CheckReturnValue;
import java.util.List;

public class GuildService {
    private final GuildDAO dao = new GuildDAO();

    @CheckReturnValue
    public GuildItem get(String name) {
        return dao.get(name);
    }

    @CheckReturnValue
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

    @CheckReturnValue
    public List<GuildItem> getAll() {
        return dao.getAll();
    }
}
