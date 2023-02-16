package org.example;

import org.example.model.*;
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
        Configuration configuration = new Configuration().addAnnotatedClass(Person.class)
                .addAnnotatedClass(Passport.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Person person = new Person("test", 50);
            Passport passport = new Passport(person, 12345);

            person.setPassport(passport);

            session.save(person);

            Person person1 = session.get(Person.class, 8);
            System.out.println(person1.getPassport().getNumber());

            Passport passport1 = session.get(Passport.class, 1);
            System.out.println(passport1);
            System.out.println(passport1.getPerson().getName());

            person1.getPassport().setNumber(777777);
            System.out.println(passport1);

            session.remove(person1);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
