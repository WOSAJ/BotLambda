package tk.wosaj.lambda.database.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import tk.wosaj.lambda.database.guild.GuildItem;

import javax.annotation.Nonnull;
import java.util.Properties;


public final class SessionFactoryGenerator {
    private SessionFactoryGenerator() {}
    private static SessionFactory sessionFactory;
    @Nonnull
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties properties = new Properties();
            properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
            //properties.put("show_sql", "true");
            try {
                Configuration configuration = new Configuration()
                        .addProperties(properties).addAnnotatedClass(GuildItem.class);
                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
