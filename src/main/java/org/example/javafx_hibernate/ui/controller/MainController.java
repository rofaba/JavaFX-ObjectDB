package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.dao.CopiaDao;
import org.example.javafx_hibernate.dao.CopiaRepository;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;

import java.util.List;

public class MainController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private TableView<Copia> tvCopias;

    @FXML
    private TableColumn<Copia, String> colTitulo;

    @FXML
    private TableColumn<Copia, String> colEstado;

    @FXML
    private TableColumn<Copia, String> colSoporte;

    private final CopiaDao copiaDao = new CopiaRepository();

    @FXML
    public void initialize() {
        // Configurar columnas
        colTitulo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPelicula().getTitulo()));

        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));

        colSoporte.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSoporte()));

        // Usuario logueado desde AuthService
        Usuario u = MainApp.getAuthService().getUsuarioActual();
        if (u != null) {
            lblBienvenida.setText("Bienvenido, " + u.getNombreUsuario() + " (" + u.getRol() + ")");
            cargarCopias(u);
        }
    }

    private void cargarCopias(Usuario usuario) {
        try {
            List<Copia> copias = copiaDao.listarPorUsuario(usuario);
            System.out.println("Copias encontradas para " + usuario.getNombreUsuario() +
                    ": " + copias.size());
            tvCopias.setItems(FXCollections.observableArrayList(copias));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void onCerrarSesion() throws Exception {
        MainApp.getAuthService().logout();
        MainApp.setRoot("login-view");
    }
}
