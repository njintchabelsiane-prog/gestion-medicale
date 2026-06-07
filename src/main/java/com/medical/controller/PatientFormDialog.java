package com.medical.controller;

import com.medical.model.Patient;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class PatientFormDialog extends Dialog<Patient> {

    private final TextField nomField = new TextField();
    private final TextField prenomField = new TextField();
    private final DatePicker dateNaissancePicker = new DatePicker();
    private final ComboBox<String> sexeCombo = new ComboBox<>();
    private final TextField telephoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextArea adresseArea = new TextArea();

    private final Patient patientExistant;

    public PatientFormDialog(Patient patient) {
        this.patientExistant = patient;

        setTitle(patient == null ? "Nouveau patient" : "Modifier patient");
        setHeaderText(patient == null ? "Ajouter un nouveau patient" : "Modifier les informations du patient");

        // Boutons
        ButtonType validerBtn = new ButtonType(patient == null ? "Ajouter" : "Modifier", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(validerBtn, ButtonType.CANCEL);

        // Configuration des champs
        sexeCombo.getItems().addAll("M", "F");
        sexeCombo.setPromptText("Sélectionner");
        adresseArea.setPrefRowCount(2);
        adresseArea.setWrapText(true);

        nomField.setPromptText("Ex: Kamga");
        prenomField.setPromptText("Ex: Paul");
        telephoneField.setPromptText("Ex: 06 12 34 56 78");
        emailField.setPromptText("Ex: patient@email.com");
        dateNaissancePicker.setPromptText("JJ/MM/AAAA");

        // Pré-remplir si modification
        if (patient != null) {
            nomField.setText(patient.getNom());
            prenomField.setText(patient.getPrenom());
            dateNaissancePicker.setValue(patient.getDateNaissance());
            sexeCombo.setValue(patient.getSexe());
            telephoneField.setText(patient.getTelephone() != null ? patient.getTelephone() : "");
            emailField.setText(patient.getEmail() != null ? patient.getEmail() : "");
            adresseArea.setText(patient.getAdresse() != null ? patient.getAdresse() : "");
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 20, 10, 20));

        grid.add(new Label("Nom *"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom *"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Date de naissance *"), 0, 2);
        grid.add(dateNaissancePicker, 1, 2);
        grid.add(new Label("Sexe *"), 0, 3);
        grid.add(sexeCombo, 1, 3);
        grid.add(new Label("Téléphone"), 0, 4);
        grid.add(telephoneField, 1, 4);
        grid.add(new Label("Email"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Adresse"), 0, 6);
        grid.add(adresseArea, 1, 6);

        Label infoLabel = new Label("* Champs obligatoires");
        infoLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        VBox content = new VBox(10, grid, infoLabel);
        VBox.setMargin(infoLabel, new Insets(0, 0, 10, 20));

        getDialogPane().setContent(content);
        getDialogPane().setPrefWidth(500);

        // Validation à la sortie
        setResultConverter(bouton -> {
            if (bouton == validerBtn) {
                return creerOuMettreAJourPatient();
            }
            return null;
        });

        // Empêcher la fermeture si validation invalide
        Button btnValider = (Button) getDialogPane().lookupButton(validerBtn);
        btnValider.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String erreur = validerChamps();
            if (erreur != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs invalides");
                alert.setHeaderText(null);
                alert.setContentText(erreur);
                alert.showAndWait();
                event.consume();
            }
        });
    }

    private String validerChamps() {
        if (nomField.getText().trim().isEmpty()) return "Le nom est obligatoire.";
        if (prenomField.getText().trim().isEmpty()) return "Le prénom est obligatoire.";
        if (dateNaissancePicker.getValue() == null) return "La date de naissance est obligatoire.";
        if (dateNaissancePicker.getValue().isAfter(LocalDate.now())) return "La date de naissance ne peut pas être dans le futur.";
        if (sexeCombo.getValue() == null) return "Le sexe est obligatoire.";
        return null;
    }

    private Patient creerOuMettreAJourPatient() {
        Patient p = (patientExistant != null) ? patientExistant : new Patient();
        p.setNom(nomField.getText().trim());
        p.setPrenom(prenomField.getText().trim());
        p.setDateNaissance(dateNaissancePicker.getValue());
        p.setSexe(sexeCombo.getValue());
        p.setTelephone(telephoneField.getText().trim());
        p.setEmail(emailField.getText().trim());
        p.setAdresse(adresseArea.getText().trim());
        return p;
    }
}