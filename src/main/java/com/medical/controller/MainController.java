package com.medical.controller;

import com.medical.App;
import com.medical.dao.PatientDAO;
import com.medical.dao.MedecinDAO;
import com.medical.dao.ConsultationDAO;
import com.medical.model.Utilisateur;
import com.medical.service.AuthService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainController {

    @FXML private Label lblUtilisateur;
    @FXML private Label lblRole;
    @FXML private Label lblDate;
    @FXML private Label lblTitre;
    @FXML private Label lblSousTitre;
    @FXML private Label lblNbPatients;
    @FXML private Label lblNbMedecins;
    @FXML private Label lblNbConsultations;
    @FXML private Label lblNbAujourdhui;

    private final PatientDAO patientDAO = new PatientDAO();
    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();

    @FXML
    public void initialize() {
        Utilisateur user = AuthService.getUtilisateurConnecte();
        if (user != null) {
            lblUtilisateur.setText(user.getPrenom() + " " + user.getNom());
            lblRole.setText(user.getRole());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);
        String dateStr = LocalDate.now().format(formatter);
        lblDate.setText(dateStr.substring(0, 1).toUpperCase() + dateStr.substring(1));

        chargerStatistiques();
    }

    private void chargerStatistiques() {
        try {
            lblNbPatients.setText(String.valueOf(patientDAO.count()));
            lblNbMedecins.setText(String.valueOf(medecinDAO.count()));
            lblNbConsultations.setText(String.valueOf(consultationDAO.count()));
            lblNbAujourdhui.setText(String.valueOf(consultationDAO.countToday()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        chargerStatistiques();
    }

    @FXML
    private void showPatients() {
        chargerModule("/view/Patients.fxml");
    }
@FXML
private void showMedecins() {
    System.out.println(">>> CLIC SUR MEDECINS DETECTE <<<");
    chargerModule("/view/Medecins.fxml");
}

   @FXML
private void showConsultations() {
    chargerModule("/view/Consultations.fxml");
}

    private void chargerModule(String fxmlPath) {
        try {
            Parent module = FXMLLoader.load(getClass().getResource(fxmlPath));
           BorderPane borderPane = (BorderPane) App.getPrimaryStage().getScene().getRoot();
            borderPane.setCenter(module);
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Erreur", "Impossible de charger le module : " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            new AuthService().logout();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = App.getPrimaryStage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion Médicale - Connexion");
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
            }
        }        