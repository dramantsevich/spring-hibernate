package org.example;

import org.example.model.Item;
import org.example.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

            Person person = session.get(Person.class, 3);
            System.out.println(person);

            List<Item> personList = person.getItemList();
            System.out.println(personList);

            Item item = session.get(Item.class, 5);
            System.out.println(item);

            Person owner = item.getPerson();
            System.out.println(owner);

            Person personWithNewItem = session.get(Person.class, 2);

            Item newItem = new Item("New Item", personWithNewItem);

            personWithNewItem.getItemList().add(newItem);

            session.save(newItem);

            Person newPerson = new Person("Test person", 45);

            Item newItemForNewPerson = new Item("New Item for New Person", newPerson);

            newPerson.setItemList(new ArrayList<>(Collections.singletonList(newItemForNewPerson)));

            session.save(newPerson);
            session.save(newItemForNewPerson);

            Person personForItemDelete = session.get(Person.class, 3);
            List<Item> itemsForDelete = personForItemDelete.getItemList();

//            SQL
            for (Item deleteItem : itemsForDelete) {
                session.remove(deleteItem);
            }

//            No SQL, but need for cash
            personForItemDelete.getItemList().clear();

            Person personDelete = session.get(Person.class, 2);

//            SQL
            session.remove(personDelete);

//            Need for cash
            personDelete.getItemList().forEach(i -> i.setPerson(null));

            Person personForSetinngItems = session.get(Person.class, 4);
            Item itemForSetPerson = session.get(Item.class, 3);
//            For cash
            itemForSetPerson.getPerson().getItemList().remove(itemForSetPerson);

//            SQL
            itemForSetPerson.setPerson(personForSetinngItems);

//            For cash
            personForSetinngItems.getItemList().add(itemForSetPerson);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
