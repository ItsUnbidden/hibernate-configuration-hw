package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.MovieDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Movie;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieDaoImpl implements MovieDao {

    @Override
    public Movie add(Movie movie) {
        Session session = null;
        Transaction transaction = null;
        Long id = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(movie);
            transaction.commit();
            id = (Long) session.getIdentifier(movie);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Was unable to add a new movie to the db.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        movie.setId(id);
        return movie;
    }

    @Override
    public Optional<Movie> get(Long id) {
        Movie movie = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            movie = session.get(Movie.class, id);
        } catch (Exception e) {
            throw new DataProcessingException(String.format(
                    "Was unable to get movie by ID %s from the db.", id), e);
        }
        return Optional.ofNullable(movie);
    }
}
