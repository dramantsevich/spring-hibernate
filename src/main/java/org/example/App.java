package org.example;

import org.example.model.*;
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
        Configuration configuration = new Configuration().addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Movie.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        //try with resources
        try(sessionFactory) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            Movie firstMovie = new Movie("Pulp fiction", 1994);
            Actor firstActor = new Actor("Harvey Keitel", 81);
            Actor secondActor = new Actor("Samuel L. Jackson", 72);

            firstMovie.setActors(new ArrayList<>(List.of(firstActor, secondActor)));

            firstActor.setMovies(new ArrayList<>(Collections.singletonList(firstMovie)));
            secondActor.setMovies(new ArrayList<>(Collections.singletonList(firstMovie)));

            session.save(firstMovie);

            session.save(firstActor);
            session.save(secondActor);

            Movie movie = session.get(Movie.class, 1);
            System.out.println(movie.getActors());

            Actor actor = session.get(Actor.class, 1);
            System.out.println(actor.getMovies());

            Movie secondMovie = new Movie("Reservoir Dogs", 1992);
            Actor existingActor = session.get(Actor.class, 1);

            secondMovie.setActors(new ArrayList<>(Collections.singletonList(existingActor)));

            existingActor.getMovies().add(secondMovie);

            session.save(secondMovie);

            Actor existingSecondActor = session.get(Actor.class, 2);
            System.out.println(existingSecondActor.getMovies());

            Movie movieToRemove = existingSecondActor.getMovies().get(0);

            existingSecondActor.getMovies().remove(0);
            movieToRemove.getActors().remove(existingSecondActor);

            session.getTransaction().commit();
        }
    }
}
