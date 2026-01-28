package org.example.javafx_objectDB.dao;

import org.example.javafx_objectDB.config.JPAUtil;
import org.example.javafx_objectDB.entity.Usuario;

import javax.persistence.EntityManager;


public class UsuarioRepository implements UsuarioDao {

    public Usuario buscarPorNombreYPassword(String nombre, String password) {
        var em = JPAUtil.em();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nombreUsuario = :n AND u.contrasena = :p",
                            Usuario.class
                    )
                    .setParameter("n", nombre)
                    .setParameter("p", password)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
    public long contarUsuarios() {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    //*     * Crea un nuevo usuario en la base de datos.
    //     * */

        public void crear(Usuario u) {
            var em = JPAUtil.em();
            var tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(u);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            } finally {
                em.close();
            }
        }

    }

