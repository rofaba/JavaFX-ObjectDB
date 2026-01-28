package org.example.javafx_objectDB.seed;

import org.example.javafx_objectDB.dao.PeliculaRepository;
import org.example.javafx_objectDB.entity.Pelicula;

import java.util.List;

public class DatabaseSeeder {
// Inserta datos iniciales en la tabla Pelicula si está vacía.

    public static void seedPeliculasIfEmpty() {
        PeliculaRepository repo = new PeliculaRepository();

        List<Pelicula> existentes = repo.listarTodas();
        if (existentes != null && !existentes.isEmpty()) {
            System.out.println("Seed Peliculas: ya existen (" + existentes.size() + "), no se inserta nada.");
            return;
        }

        // Seed mínimo (tus pelis)
        repo.guardar(new Pelicula(
                "The Matrix",
                "Ciencia ficción",
                1999,
                "Un hacker descubre la verdad sobre su realidad.",
                "Lana & Lilly Wachowski",
                "/images/peliculas/matrix.jpg"
        ));

        repo.guardar(new Pelicula(
                "Inception",
                "Acción",
                2010,
                "Un ladrón especializado roba información infiltrándose en los sueños.",
                "Christopher Nolan",
                "/images/peliculas/inception.jpg"
        ));

        repo.guardar(new Pelicula(
                "Interstellar",
                "Ciencia ficción",
                2014,
                "Un equipo viaja a través de un agujero de gusano para salvar a la humanidad.",
                "Christopher Nolan",
                "/images/peliculas/interstellar.jpg"
        ));

        repo.guardar(new Pelicula(
                "Gladiator",
                "Drama",
                2000,
                "Un general romano traicionado busca venganza.",
                "Ridley Scott",
                "/images/peliculas/gladiator.jpg"
        ));

        repo.guardar(new Pelicula(
                "The Dark Knight",
                "Acción",
                2008,
                "Batman se enfrenta al Joker en una ciudad al borde del caos.",
                "Christopher Nolan",
                "/images/peliculas/darkknight.jpg"
        ));

        repo.guardar(new Pelicula(
                "Fight Club",
                "Suspenso",
                1999,
                "Un hombre crea un club clandestino que se sale de control.",
                "David Fincher",
                "/images/peliculas/fightclub.jpg"
        ));

        repo.guardar(new Pelicula(
                "Mad Max: Fury Road",
                "Acción",
                2015,
                "En un desierto postapocalíptico, Max y Furiosa huyen de un tirano.",
                "George Miller",
                "/images/peliculas/madmax.jpg"
        ));

        repo.guardar(new Pelicula(
                "Silver Linings Playbook",
                "Drama",
                2012,
                "Tras salir de una institución psiquiátrica, Pat intenta reconstruir su vida.",
                "David O. Russell",
                "/images/peliculas/silverlinings.jpg"
    ));

        System.out.println("Seed Peliculas: datos iniciales insertados en la base de datos.");
    }
}
