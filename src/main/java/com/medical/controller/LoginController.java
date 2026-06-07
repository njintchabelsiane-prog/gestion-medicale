package com.medical.controller;

import com.medical.model.Utilisateur;
import com.medical.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
            // TODO : ouvrir la fenêtre principale (Dashboard)
        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}