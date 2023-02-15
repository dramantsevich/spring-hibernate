package org.example;

import org.example.model.Director;
import org.example.model.Item;
import org.example.model.Movie;
import org.example.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Configuration configuration = new Configuration().addAnnotatedClass(Person.class)
                .addAnnotatedClass(Item.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Person person = new Person("Test cascading", 25);

            Item item = new Item("Test cascading", person);
            person.setItemList(new ArrayList<>(Collections.singletonList(item)));

            session.persist(person);
            session.save(person);


            Person person1 = new Person("Test cascading", 30);

            person1.addItem(new Item("Item1"));
            person1.addItem(new Item("Item2"));
            person1.addItem(new Item("Item3"));

            session.save(person1);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
