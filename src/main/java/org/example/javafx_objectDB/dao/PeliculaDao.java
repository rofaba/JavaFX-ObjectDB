package org.example.javafx_objectDB.dao;


import org.example.javafx_objectDB.entity.Pelicula;
import java.util.List;

/*
    * Interfaz DAO para la entidad Pelicula.
 */
public interface PeliculaDao {
    void guardar(Pelicula pelicula);
    List<Pelicula> listarTodas();
    int deleteById(Long id);
}