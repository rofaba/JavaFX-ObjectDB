package org.example.javafx_objectDB;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javafx_objectDB.config.JPAUtil;
import org.example.javafx_objectDB.dao.UsuarioRepository;
import org.example.javafx_objectDB.entity.Usuario;
import org.example.javafx_objectDB.seed.DatabaseSeeder;
import org.example.javafx_objectDB.service.AuthService;
import javafx.application.Platform;
import java.io.IOException;

public class MainApp extends Application {


    private static Stage primaryStage;
    private static AuthService authService = new AuthService();

    public static AuthService getAuthService() {
        return authService;
    }



    @Override
    public void start(Stage stage) throws IOException {

        //test temporal usuarios guardados en la base de datos o crear el admin
        UsuarioRepository repo = new UsuarioRepository();

        if (repo.contarUsuarios() == 0) {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin");
            admin.setContrasena("1234");
            admin.setRol("ADMIN");

            Usuario user = new Usuario();
            user.setNombreUsuario("user");
            user.setContrasena("1234");
            user.setRol("USER");

            Usuario user2 = new Usuario();
            user2.setNombreUsuario("user2");
            user2.setContrasena("1234");
            user2.setRol("USER");

            repo.crear(admin);
            repo.crear(user);
            repo.crear(user2);

            System.out.println("Usuarios admin y user iniciales creados en la base de datos.");
        }
        //seed peliculas si la tabla esta vacia
        DatabaseSeeder.seedPeliculasIfEmpty();

        //app main window
        primaryStage = stage;
        primaryStage.setTitle("Películas - Login");
        setRoot("login-view");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/javafx_objectDB/" + fxml + ".fxml")
        );
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }
    //crear funcion que compruebe la conexion con la bae de datos
    public static boolean comprobarConexionBD() {
        try {
            return authService.comprobarConexion();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void stop() {
        org.example.javafx_objectDB.config.JPAUtil.close();
    }



    public static void main(String[] args) {
        launch(args);
    }
}