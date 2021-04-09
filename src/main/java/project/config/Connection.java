package project.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Connection {
    private static SessionFactory sessionFactory;

    public static Session getSession() {
        if (sessionFactory != null) {
            return sessionFactory.openSession();
        }
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        Metadata metadata = new MetadataSources(registry)
                .getMetadataBuilder()
                .build();
        sessionFactory = metadata.getSessionFactoryBuilder()
                .build();
        return sessionFactory.openSession();
    }
}
