package com.medical.controller;

import com.medical.App;
import com.medical.model.Utilisateur;
import com.medical.service.AuthService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur user = authService.login(email, password);

        if (user != null) {
            errorLabel.setText("");
            System.out.println("Bienvenue " + user.getPrenom() + " !");
            ouvrirDashboard();
        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }

    private void ouvrirDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = App.getPrimaryStage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion Médicale - Tableau de bord");
            stage.setResizable(true);
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de l'ouverture du tableau de bord.");
        }
    }
}