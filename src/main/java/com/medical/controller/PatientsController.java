package com.medical.controller;

import com.medical.dao.PatientDAO;
import com.medical.model.Patient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PatientsController {

    @FXML private TextField searchField;
    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, String> colDateNaissance;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colSexe;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TableColumn<Patient, String> colEmail;

    private final PatientDAO patientDAO = new PatientDAO();
    private final ObservableList<Patient> patientsList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdPatient()).asObject());
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        colDateNaissance.setCellValueFactory(data -> {
            if (data.getValue().getDateNaissance() != null) {
                return new SimpleStringProperty(data.getValue().getDateNaissance().format(dateFormat));
            }
            return new SimpleStringProperty("");
        });
        colAge.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAge()).asObject());
        colSexe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSexe()));
        colTelephone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        tablePatients.setItems(patientsList);

        chargerPatients();
    }

    private void chargerPatients() {
        try {
            List<Patient> patients = patientDAO.findAll();
            patientsList.setAll(patients);
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur", "Impossible de charger les patients : " + e.getMessage());
        }
    }

    @FXML
    private void handleRechercher() {
        String recherche = searchField.getText().trim();
        if (recherche.isEmpty()) {
            chargerPatients();
            return;
        }
        try {
            List<Patient> resultats = patientDAO.search(recherche);
            patientsList.setAll(resultats);
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }

    @FXML
    private void handleReinitialiser() {
        searchField.clear();
        chargerPatients();
    }

    @FXML
    private void handleAjouter() {
        PatientFormDialog dialog = new PatientFormDialog(null);
        Optional<Patient> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                patientDAO.create(resultat.get());
                chargerPatients();
                afficherInfo("Succès", "Patient ajouté avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible d'ajouter le patient : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Patient selectionne = tablePatients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune sélection", "Veuillez sélectionner un patient à modifier.");
            return;
        }
        PatientFormDialog dialog = new PatientFormDialog(selectionne);
        Optional<Patient> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                patientDAO.update(resultat.get());
                chargerPatients();
                afficherInfo("Succès", "Patient modifié avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de modifier le patient : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Patient selectionne = tablePatients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune sélection", "Veuillez sélectionner un patient à supprimer.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer le patient ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer " + selectionne.getPrenom() + " " + selectionne.getNom() + " ?\nCette action est irréversible.");
        Optional<ButtonType> reponse = confirmation.showAndWait();
        if (reponse.isPresent() && reponse.get() == ButtonType.OK) {
            try {
                patientDAO.delete(selectionne.getIdPatient());
                chargerPatients();
                afficherInfo("Succès", "Patient supprimé avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de supprimer le patient : " + e.getMessage());
            }
        }
    }

    private void afficherInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}