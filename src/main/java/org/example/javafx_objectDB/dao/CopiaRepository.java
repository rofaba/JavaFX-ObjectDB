package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.JPAUtil;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CopiaRepository implements CopiaDao {

    @Override
    public List<Copia> listarPorUsuario(Usuario usuario) throws Exception {
        EntityManager em = JPAUtil.em();
        try {
            // JOIN FETCH para traer también la película asociada
            TypedQuery<Copia> q = em.createQuery(
                    "SELECT c FROM Copia c " +
                            "JOIN FETCH c.pelicula " +
                            "WHERE c.usuario = :usuario",
                    Copia.class
            );
            q.setParameter("usuario", usuario);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarCopia(Integer id) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();

            Copia copia = em.find(Copia.class, id);
            if (copia != null) {
                if (copia.getCantidad() > 1) {
                    copia.setCantidad(copia.getCantidad() - 1);
                    em.merge(copia);
                } else {
                    em.remove(copia);
                }
            }

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
