package org.example.javafx_objectDB.entity;

import javax.persistence.*;

@Entity
@Table(name = "pelicula")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String genero;
    private Integer anio;

    @Column(length = 2000)
    private String descripcion;

    private String director;

    // Ej: "/images/peliculas/matrix.jpg"
    private String imagen;

    public Pelicula() {}

    public Pelicula(String titulo, String genero, Integer anio, String descripcion, String director, String imagen) {
        this.titulo = titulo;
        this.genero = genero;
        this.anio = anio;
        this.descripcion = descripcion;
        this.director = director;
        this.imagen = imagen;
    }

    public Long getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
