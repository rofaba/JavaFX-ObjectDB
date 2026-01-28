package org.example.javafx_objectDB.entity;

import javax.persistence.*;

@Entity
@Table(name = "copia")
public class Copia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String estado;  // ej: "Nueva", "Usada"

    @Column(nullable = false)
    private String soporte; // ej: "DVD", "BluRay", "Digital"

    @Column(nullable = false)
    private Integer cantidad = 1;

    public Copia() {}

    public Integer getId() { return id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getSoporte() { return soporte; }
    public void setSoporte(String soporte) { this.soporte = soporte; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
