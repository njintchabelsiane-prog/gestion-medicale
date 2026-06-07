package com.medical;

import com.medical.service.AuthService;
import com.medical.model.Utilisateur;
import com.medical.util.DatabaseConnection;

public class App {
    public static void main(String[] args) {
        System.out.println("=== TEST LOGIN ===\n");

        AuthService authService = new AuthService();
        Utilisateur admin = authService.login("admin@medical.fr", "admin123");
        
        if (admin != null) {
            System.out.println("✅ Login réussi !");
            System.out.println("Utilisateur : " + AuthService.getUtilisateurConnecte());
            System.out.println("Est admin ? " + AuthService.estAdmin());
        }

        DatabaseConnection.closeConnection();
    }
}