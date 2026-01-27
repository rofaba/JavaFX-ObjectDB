package org.example.javafx_objectDB;

import org.example.javafx_objectDB.service.AuthService;
import org.example.javafx_objectDB.entity.Usuario;

public class TestLogin {

    public static void main(String[] args) {
        AuthService authService = new AuthService();

        boolean ok = authService.login("admin", "admin"); // datos del script SQL

        if (ok) {
            Usuario u = authService.getUsuarioActual();
            System.out.println("Login correcto: " + u.getNombreUsuario() + " - Rol: " + u.getRol());
        } else {
            System.out.println("Credenciales inválidas");
        }
    }
}
