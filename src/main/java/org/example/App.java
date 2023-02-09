package org.example;

import org.example.model.Director;
import org.example.model.Movie;
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
        Configuration configuration = new Configuration().addAnnotatedClass(Director.class)
                .addAnnotatedClass(Movie.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Director director = session.get(Director.class, 1);
            System.out.println(director);

            List<Movie> movieList = director.getMovieList();
            System.out.println(movieList);

            Movie movie = session.get(Movie.class, 1);
            System.out.println(movie);

            Director movieDirector = movie.getDirector();
            System.out.println(movieDirector);

            Director directorWithNewMovie = session.get(Director.class, 2);
            Movie newMovie = new Movie("New movie", directorWithNewMovie, 1999);

            directorWithNewMovie.getMovieList().add(newMovie);

            session.save(newMovie);

            Director newDirector = new Director("Dima", 2000);
            Movie newMovieForDirector = new Movie("HEHEHEHEHE", newDirector, 2005);

            newDirector.setMovieList(new ArrayList<>(Collections.singletonList(newMovieForDirector)));

            session.save(newDirector);
            session.save(newMovieForDirector);

            Movie existingMovie = session.get(Movie.class, 3);
            Director existingDirector = session.get(Director.class, 7);

            session.createQuery("UPDATE Movie SET director_id =" + existingDirector.getDirector_id()
                    + " WHERE movie_id =" + existingMovie.getId()).executeUpdate();

            Director directorWhichFilmRemove = session.get(Director.class, 4);
            Movie movieForDelete = session.get(Movie.class, directorWhichFilmRemove.getMovieList().get(0).getId());

            directorWhichFilmRemove.getMovieList().remove(movieForDelete);
            session.delete(movieForDelete);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
