package com.medical.controller;

import com.medical.dao.ConsultationDAO;
import com.medical.model.Consultation;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ConsultationsController {

    @FXML private TableView<Consultation> tableConsultations;
    @FXML private TableColumn<Consultation, Integer> colId;
    @FXML private TableColumn<Consultation, String> colDate;
    @FXML private TableColumn<Consultation, String> colPatient;
    @FXML private TableColumn<Consultation, String> colMedecin;
    @FXML private TableColumn<Consultation, String> colMotif;
    @FXML private TableColumn<Consultation, String> colDiagnostic;

    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final ObservableList<Consultation> consultationsList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdConsultation()).asObject());
        colDate.setCellValueFactory(data -> {
            if (data.getValue().getDateConsultation() != null) {
                return new SimpleStringProperty(data.getValue().getDateConsultation().format(dateFormat));
            }
            return new SimpleStringProperty("");
        });
        colPatient.setCellValueFactory(data -> {
            Consultation c = data.getValue();
            if (c.getPatient() != null) {
                return new SimpleStringProperty(c.getPatient().getPrenom() + " " + c.getPatient().getNom());
            }
            return new SimpleStringProperty("");
        });
        colMedecin.setCellValueFactory(data -> {
            Consultation c = data.getValue();
            if (c.getMedecin() != null) {
                return new SimpleStringProperty("Dr " + c.getMedecin().getPrenom() + " " + c.getMedecin().getNom());
            }
            return new SimpleStringProperty("");
        });
        colMotif.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotif()));
        colDiagnostic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDiagnostic()));

        tableConsultations.setItems(consultationsList);
        chargerConsultations();
    }

    private void chargerConsultations() {
        try {
            List<Consultation> consultations = consultationDAO.findAll();
            consultationsList.setAll(consultations);
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur", "Impossible de charger les consultations : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        ConsultationFormDialog dialog = new ConsultationFormDialog(null);
        Optional<Consultation> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                consultationDAO.create(resultat.get());
                chargerConsultations();
                afficherInfo("Succes", "Consultation ajoutee avec succes.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible d'ajouter la consultation : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Consultation selectionne = tableConsultations.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune selection", "Veuillez selectionner une consultation a modifier.");
            return;
        }
        ConsultationFormDialog dialog = new ConsultationFormDialog(selectionne);
        Optional<Consultation> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                consultationDAO.update(resultat.get());
                chargerConsultations();
                afficherInfo("Succes", "Consultation modifiee avec succes.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de modifier la consultation : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Consultation selectionne = tableConsultations.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune selection", "Veuillez selectionner une consultation a supprimer.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la consultation ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer cette consultation ?\nCette action est irreversible.");
        Optional<ButtonType> reponse = confirmation.showAndWait();
        if (reponse.isPresent() && reponse.get() == ButtonType.OK) {
            try {
                consultationDAO.delete(selectionne.getIdConsultation());
                chargerConsultations();
                afficherInfo("Succes", "Consultation supprimee avec succes.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de supprimer la consultation : " + e.getMessage());
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