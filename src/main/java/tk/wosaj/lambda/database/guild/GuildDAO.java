package tk.wosaj.lambda.database.guild;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tk.wosaj.lambda.database.util.SessionFactoryGenerator;

import java.util.List;

public class GuildDAO {
    public GuildItem get(String name) {
        try(Session session = SessionFactoryGenerator.getSessionFactory().openSession()) {
            return session.get(GuildItem.class, name);
        }
    }

    public void save(GuildItem item) {
        Session session = SessionFactoryGenerator.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(item);
        tx1.commit();
        session.close();
    }

    public void update(GuildItem item) {
        Session session = SessionFactoryGenerator.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(item);
        tx1.commit();
        session.close();
    }

    public void delete(GuildItem item) {
        Session session = SessionFactoryGenerator.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(item);
        tx1.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    public List<GuildItem> getAll() {
        return (List<GuildItem>)  SessionFactoryGenerator.getSessionFactory().openSession().createQuery("From GuildItem").list();
    }
}
