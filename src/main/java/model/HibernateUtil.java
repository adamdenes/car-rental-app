package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    // loads the properties file from the classpath and uses it to configure `Hibernate`
    static {
        try {
            InputStream inputStream = HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            Configuration configuration = new Configuration()
                    .addProperties(properties)
                    .addAnnotatedClass(Car.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // obtain a `Session` instance to perform db operations
    public static Session getSession() {
        return sessionFactory.openSession();
    }
}