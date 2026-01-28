package org.example.javafx_objectDB.dao;

import org.example.javafx_objectDB.config.JPAUtil;
import org.example.javafx_objectDB.entity.Pelicula;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PeliculaRepository implements PeliculaDao {

    @Override
    public void guardar(Pelicula p) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();

            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pelicula p WHERE LOWER(p.titulo) = LOWER(:titulo)",
                            Long.class
                    ).setParameter("titulo", p.getTitulo())
                    .getSingleResult();

            if (count != null && count > 0) {
                em.getTransaction().rollback();
                throw new RuntimeException("Ya existe una película con el título: " + p.getTitulo());

            }

            em.persist(p);
            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pelicula> listarTodas() {
        EntityManager em = JPAUtil.em();
        try {
            TypedQuery<Pelicula> q = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Long deleteById(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();

            int deletedCount = em.createQuery(
                            "DELETE FROM Pelicula p WHERE p.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            em.getTransaction().commit();
            return (long) deletedCount;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}
