package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Pelicula;
import org.example.javafx_hibernate.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class NuevaCopiaController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private ComboBox<String> cbSoporte;

    @FXML
    private Spinner<Integer> spCantidad;


    // Para poder pedir al MainController que recargue la tabla
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        // Valores fijos según tu comentario
        cbEstado.setItems(FXCollections.observableArrayList("BUENO", "DAÑADO"));
        cbSoporte.setItems(FXCollections.observableArrayList("DVD", "BLURAY", "VHS"));
        spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1)
        );

    }

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

        // Usuario logueado
        Usuario usuario = MainApp.getAuthService().getUsuarioActual();
        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "No hay usuario logueado.");
            return;
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Buscar película por título
            Query<Pelicula> query = session.createQuery(
                    "FROM Pelicula p WHERE p.titulo = :titulo", Pelicula.class);
            query.setParameter("titulo", titulo);
            Pelicula pelicula = query.uniqueResult();

            if (pelicula == null) {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "No se encontró ninguna película con ese título.\n" +
                                "Primero debe existir la película en el catálogo.");
                tx.rollback();
                return;
            }
            //si la copia exacta existe debe actualizar cantidad
            Query<Copia> copiaQuery = session.createQuery(
                    "FROM Copia c WHERE c.pelicula = :pelicula " +
                            "AND c.usuario = :usuario " +
                            "AND c.estado = :estado " +
                            "AND c.soporte = :soporte", Copia.class);
            copiaQuery.setParameter("pelicula", pelicula);
            copiaQuery.setParameter("usuario", usuario);
            copiaQuery.setParameter("estado", estado);
            copiaQuery.setParameter("soporte", soporte);
            Copia copiaExistente = copiaQuery.uniqueResult();
            if (copiaExistente != null) {
                copiaExistente.setCantidad(copiaExistente.getCantidad() + cantidad);
                session.update(copiaExistente);
                tx.commit();
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Copia existente actualizada correctamente.");
                // Recargar tabla en MainController
                if (mainController != null) {
                    mainController.cargarCopias(usuario);
                }
                cerrarVentana();
                return;
            }
            // Crear nueva copia

            Copia copia = new Copia();
            copia.setPelicula(pelicula);
            copia.setUsuario(usuario);
            copia.setEstado(estado);
            copia.setSoporte(soporte);
            copia.setCantidad(cantidad);

            session.persist(copia);
            tx.commit();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Copia guardada correctamente.");

            // Recargar tabla en MainController
            if (mainController != null) {
                mainController.cargarCopias(usuario);
            }

            cerrarVentana();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar la copia.");
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
}
