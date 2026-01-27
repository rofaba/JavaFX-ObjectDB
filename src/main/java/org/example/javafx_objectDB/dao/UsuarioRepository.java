package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.JPAUtil;
import org.example.javafx_hibernate.entity.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UsuarioRepository implements UsuarioDao {

    @Override
    public Usuario buscarPorNombreYPassword(String nombreUsuario, String password) throws Exception {
        EntityManager em = JPAUtil.em();
        try {
            // No hace falta transacción para SELECT (aunque ObjectDB lo tolera)
            TypedQuery<Usuario> q = em.createQuery(
                    "SELECT u FROM Usuario u " +
                            "WHERE u.nombreUsuario = :nombre AND u.contrasena = :pass",
                    Usuario.class
            );
            q.setParameter("nombre", nombreUsuario);
            q.setParameter("pass", password);

            List<Usuario> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);

        } finally {
            em.close();
        }
    }
}
