package org.example.javafx_objectDB.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx_objectDB.MainApp;
import org.example.javafx_objectDB.config.JPAUtil;
import org.example.javafx_objectDB.entity.Copia;
import org.example.javafx_objectDB.entity.Pelicula;
import org.example.javafx_objectDB.entity.Usuario;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class NuevaCopiaController {

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> cbEstado;
    @FXML private ComboBox<String> cbSoporte;
    @FXML private Spinner<Integer> spCantidad;

    private Copia copiaEnEdicion; // null = NUEVA, no null = EDITAR
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
/*    * Inicializa los componentes de la vista, configurando los valores del ComboBox y Spinner.
 */
    @FXML
    private void initialize() {
        cbEstado.setItems(FXCollections.observableArrayList("BUENO", "DAÑADO"));
        cbSoporte.setItems(FXCollections.observableArrayList("DVD", "BLURAY", "VHS"));
        spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
    }
/*    * Maneja el evento de guardar una copia.
     * Valida los campos, busca la película por título y guarda o actualiza la copia en la base de datos.
     * También maneja la lógica para evitar duplicados y actualizar cantidades.
 */
    @FXML
    private void onGuardar() {
        String titulo = txtTitulo.getText() != null ? txtTitulo.getText().trim() : "";
        String estado = cbEstado.getValue();
        String soporte = cbSoporte.getValue();
        Integer cantidad = spCantidad.getValue();

        if (titulo.isEmpty() || estado == null || soporte == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe completar todos los campos.");
            return;
        }

        Usuario usuario = MainApp.getAuthService().getUsuarioActual();
        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "No hay usuario logueado.");
            return;
        }

        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();

            // Buscar película por título
            TypedQuery<Pelicula> qPeli = em.createQuery(
                    "SELECT p FROM Pelicula p WHERE p.titulo = :titulo",
                    Pelicula.class
            );
            qPeli.setParameter("titulo", titulo);

            List<Pelicula> pelis = qPeli.getResultList();
            Pelicula pelicula = pelis.isEmpty() ? null : pelis.get(0);

            if (pelicula == null) {
                em.getTransaction().rollback();
                mostrarAlerta(Alert.AlertType.ERROR, "La película con título '" + titulo + "' no existe.");
                return;
            }

            if (copiaEnEdicion != null) {
                // EDITAR: traemos la copia gestionada y actualizamos campos
                Copia copiaGestionada = em.find(Copia.class, copiaEnEdicion.getId());
                if (copiaGestionada == null) {
                    em.getTransaction().rollback();
                    mostrarAlerta(Alert.AlertType.ERROR, "La copia a editar ya no existe.");
                    return;
                }

                copiaGestionada.setEstado(estado);
                copiaGestionada.setSoporte(soporte);
                copiaGestionada.setCantidad(cantidad);

                em.getTransaction().commit();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Copia actualizada correctamente.");

            } else {
                // NUEVA: buscar si ya existe una copia idéntica
                TypedQuery<Copia> qCopia = em.createQuery(
                        "SELECT c FROM Copia c " +
                                "WHERE c.pelicula = :pelicula " +
                                "AND c.usuario = :usuario " +
                                "AND c.estado = :estado " +
                                "AND c.soporte = :soporte",
                        Copia.class
                );
                qCopia.setParameter("pelicula", pelicula);
                qCopia.setParameter("usuario", usuario);
                qCopia.setParameter("estado", estado);
                qCopia.setParameter("soporte", soporte);

                List<Copia> existentes = qCopia.getResultList();
                Copia copiaExistente = existentes.isEmpty() ? null : existentes.get(0);

                if (copiaExistente != null) {
                    copiaExistente.setCantidad(copiaExistente.getCantidad() + cantidad);
                    em.merge(copiaExistente);

                    em.getTransaction().commit();
                    mostrarAlerta(Alert.AlertType.INFORMATION,
                            "La copia ya existía. Cantidad aumentada a " + copiaExistente.getCantidad() + ".");
                } else {
                    Copia nuevaCopia = new Copia();
                    nuevaCopia.setPelicula(pelicula);
                    nuevaCopia.setUsuario(usuario);
                    nuevaCopia.setEstado(estado);
                    nuevaCopia.setSoporte(soporte);
                    nuevaCopia.setCantidad(cantidad);

                    em.persist(nuevaCopia);

                    em.getTransaction().commit();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Copia guardada correctamente.");
                }
            }

            // Recargar tabla en MainController
            if (mainController != null) {
                mainController.cargarCopias(usuario);
            }

            cerrarVentana();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar la copia.");
        } finally {
            em.close();
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setCopiaEnEdicion(Copia copia) {
        this.copiaEnEdicion = copia;

        txtTitulo.setText(copia.getPelicula().getTitulo());
        txtTitulo.setDisable(true);

        cbEstado.setValue(copia.getEstado());
        cbSoporte.setValue(copia.getSoporte());
        spCantidad.getValueFactory().setValue(copia.getCantidad());
    }
}
