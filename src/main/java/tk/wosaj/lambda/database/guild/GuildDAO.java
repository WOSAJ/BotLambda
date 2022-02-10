package tk.wosaj.lambda.database.guild;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static tk.wosaj.lambda.database.util.SessionFactoryGenerator.getSessionFactory;

public class GuildDAO implements IGuildDAO {
    @Override
    public GuildItem get(String name) {
        return getSessionFactory().openSession().get(GuildItem.class, name);
    }

    @Override
    public void save(GuildItem item) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
        }
    }

    @Override
    public void update(GuildItem item) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(item);
            transaction.commit();
        }
    }

    @Override
    public void delete(GuildItem item) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(item);
            transaction.commit();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GuildItem> getAll() {
        return (List<GuildItem>) getSessionFactory().openSession().createQuery("FROM GuildItem").list();
    }
}
