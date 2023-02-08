package org.example;

import org.example.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Configuration configuration = new Configuration().addAnnotatedClass(Person.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Person person1 = session.get(Person.class, 2);

            session.delete(person1);

            Person person2 = new Person("Dima", 22);
            session.save(person2);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
