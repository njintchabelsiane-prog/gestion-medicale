package com.medical.controller;

import com.medical.dao.MedecinDAO;
import com.medical.model.Medecin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class MedecinsController {

    @FXML private TableView<Medecin> tableMedecins;
    @FXML private TableColumn<Medecin, Integer> colId;
    @FXML private TableColumn<Medecin, String> colNom;
    @FXML private TableColumn<Medecin, String> colPrenom;
    @FXML private TableColumn<Medecin, String> colSpecialite;
    @FXML private TableColumn<Medecin, String> colTelephone;
    @FXML private TableColumn<Medecin, String> colEmail;

    private final MedecinDAO medecinDAO = new MedecinDAO();
    private final ObservableList<Medecin> medecinsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdMedecin()).asObject());
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        colSpecialite.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpecialite()));
        colTelephone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        tableMedecins.setItems(medecinsList);
        chargerMedecins();
    }

    private void chargerMedecins() {
        try {
            List<Medecin> medecins = medecinDAO.findAll();
            medecinsList.setAll(medecins);
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur", "Impossible de charger les médecins : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        MedecinFormDialog dialog = new MedecinFormDialog(null);
        Optional<Medecin> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                medecinDAO.create(resultat.get());
                chargerMedecins();
                afficherInfo("Succès", "Médecin ajouté avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible d'ajouter le médecin : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Medecin selectionne = tableMedecins.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune sélection", "Veuillez sélectionner un médecin à modifier.");
            return;
        }
        MedecinFormDialog dialog = new MedecinFormDialog(selectionne);
        Optional<Medecin> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                medecinDAO.update(resultat.get());
                chargerMedecins();
                afficherInfo("Succès", "Médecin modifié avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de modifier le médecin : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Medecin selectionne = tableMedecins.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherInfo("Aucune sélection", "Veuillez sélectionner un médecin à supprimer.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer le médecin ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer Dr " + selectionne.getPrenom() + " " + selectionne.getNom() + " ?\nCette action est irréversible.");
        Optional<ButtonType> reponse = confirmation.showAndWait();
        if (reponse.isPresent() && reponse.get() == ButtonType.OK) {
            try {
                medecinDAO.delete(selectionne.getIdMedecin());
                chargerMedecins();
                afficherInfo("Succès", "Médecin supprimé avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur", "Impossible de supprimer le médecin : " + e.getMessage());
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