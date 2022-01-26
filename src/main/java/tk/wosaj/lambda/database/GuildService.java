package tk.wosaj.lambda.database;

import java.util.List;

public class GuildService implements IGuildDAO {
    private final GuildDAO dao = new GuildDAO();

    @Override
    public GuildItem get(String name) {
        return dao.get(name);
    }

    @Override
    public void save(GuildItem item) {
        dao.save(item);
    }

    @Override
    public void update(GuildItem item) {
        dao.update(item);
    }

    @Override
    public void delete(GuildItem item) {
        dao.delete(item);
    }

    @Override
    public List<GuildItem> getAll() {
        return dao.getAll();
    }
}
