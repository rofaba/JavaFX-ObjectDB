package org.example.javafx_objectDB.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx_objectDB.dao.PeliculaDao;
import org.example.javafx_objectDB.dao.PeliculaRepository;
import org.example.javafx_objectDB.entity.Pelicula;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevaPeliculaController implements Initializable {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDirector;
    @FXML private Spinner<Integer> spAnio;
    @FXML private TextField txtGenero;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtImagen;

    private final PeliculaDao peliculaDao = new PeliculaRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar Spinner para años (ej. 1900 a 2100, valor por defecto 2023)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, 2023);
        spAnio.getEditor().setPromptText("Ej: 1999");
        spAnio.setValueFactory(valueFactory);
    }

/*    * Maneja el evento de guardar una nueva película.
        Valida los campos y guarda la película en la base de datos.
 */
    @FXML
    public void onGuardar() {
        if (txtTitulo.getText().isEmpty() || txtDirector.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "El título y el director son obligatorios.");
            return;
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(txtTitulo.getText());
        pelicula.setDirector(txtDirector.getText());
        pelicula.setAnio(spAnio.getValue());
        pelicula.setGenero(txtGenero.getText());
        pelicula.setDescripcion(txtDescripcion.getText());
        String imagen=txtImagen.getText().trim();

        /* Establece la ruta de la imagen si se proporciona, en caso contrario null. */
        if (!imagen.isEmpty()) {
            String rutaImagen = "/images/peliculas/"+imagen;
            pelicula.setImagen(rutaImagen);
        } else {
            pelicula.setImagen(null);
        }

        try {
            peliculaDao.guardar(pelicula);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Pelìcula guardada correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Pelicula ya existe en la base de datos");
        }
    }

    @FXML
    public void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }


/*    * Muestra una alerta con el título y mensaje proporcionados.
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
 */

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}