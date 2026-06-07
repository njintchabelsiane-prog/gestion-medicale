package com.medical.controller;

import com.medical.dao.MedecinDAO;
import com.medical.dao.PatientDAO;
import com.medical.model.Consultation;
import com.medical.model.Medecin;
import com.medical.model.Patient;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ConsultationFormDialog extends Dialog<Consultation> {

    private final ComboBox<Patient> patientCombo = new ComboBox<>();
    private final ComboBox<Medecin> medecinCombo = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();
    private final TextField heureField = new TextField();
    private final TextField motifField = new TextField();
    private final TextArea diagnosticArea = new TextArea();
    private final TextArea traitementArea = new TextArea();

    private final Consultation consultationExistante;

    public ConsultationFormDialog(Consultation consultation) {
        this.consultationExistante = consultation;

        setTitle(consultation == null ? "Nouvelle consultation" : "Modifier consultation");
        setHeaderText(consultation == null ? "Enregistrer une nouvelle consultation" : "Modifier la consultation");

        ButtonType validerBtn = new ButtonType(consultation == null ? "Enregistrer" : "Modifier", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(validerBtn, ButtonType.CANCEL);

        // Charger patients et médecins
        try {
            List<Patient> patients = new PatientDAO().findAll();
            patientCombo.getItems().addAll(patients);
            patientCombo.setConverter(new StringConverter<Patient>() {
                @Override
                public String toString(Patient p) {
                    return p == null ? "" : p.getPrenom() + " " + p.getNom();
                }
                @Override
                public Patient fromString(String s) { return null; }
            });

            List<Medecin> medecins = new MedecinDAO().findAll();
            medecinCombo.getItems().addAll(medecins);
            medecinCombo.setConverter(new StringConverter<Medecin>() {
                @Override
                public String toString(Medecin m) {
                    return m == null ? "" : "Dr " + m.getPrenom() + " " + m.getNom() + " (" + m.getSpecialite() + ")";
                }
                @Override
                public Medecin fromString(String s) { return null; }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        patientCombo.setPromptText("Selectionner un patient");
        medecinCombo.setPromptText("Selectionner un medecin");
        datePicker.setPromptText("JJ/MM/AAAA");
        heureField.setPromptText("Ex: 14:30");
        motifField.setPromptText("Ex: Consultation de routine");
        diagnosticArea.setPromptText("Diagnostic etabli");
        traitementArea.setPromptText("Traitement prescrit");

        diagnosticArea.setPrefRowCount(2);
        traitementArea.setPrefRowCount(2);
        diagnosticArea.setWrapText(true);
        traitementArea.setWrapText(true);

        patientCombo.setPrefWidth(300);
        medecinCombo.setPrefWidth(300);

        // Pré-remplir si modification
        if (consultation != null) {
            for (Patient p : patientCombo.getItems()) {
                if (consultation.getPatient() != null && p.getIdPatient() == consultation.getPatient().getIdPatient()) {
                    patientCombo.setValue(p);
                    break;
                }
            }
            for (Medecin m : medecinCombo.getItems()) {
                if (consultation.getMedecin() != null && m.getIdMedecin() == consultation.getMedecin().getIdMedecin()) {
                    medecinCombo.setValue(m);
                    break;
                }
            }
            if (consultation.getDateConsultation() != null) {
                datePicker.setValue(consultation.getDateConsultation().toLocalDate());
                heureField.setText(consultation.getDateConsultation().toLocalTime().toString().substring(0, 5));
            }
            motifField.setText(consultation.getMotif() != null ? consultation.getMotif() : "");
            diagnosticArea.setText(consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "");
            traitementArea.setText(consultation.getTraitement() != null ? consultation.getTraitement() : "");
        } else {
            heureField.setText("09:00");
        }

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 20, 10, 20));

        grid.add(new Label("Patient *"), 0, 0);
        grid.add(patientCombo, 1, 0);
        grid.add(new Label("Medecin *"), 0, 1);
        grid.add(medecinCombo, 1, 1);
        grid.add(new Label("Date *"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Heure *"), 0, 3);
        grid.add(heureField, 1, 3);
        grid.add(new Label("Motif *"), 0, 4);
        grid.add(motifField, 1, 4);
        grid.add(new Label("Diagnostic"), 0, 5);
        grid.add(diagnosticArea, 1, 5);
        grid.add(new Label("Traitement"), 0, 6);
        grid.add(traitementArea, 1, 6);

        Label infoLabel = new Label("* Champs obligatoires");
        infoLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        VBox content = new VBox(10, grid, infoLabel);
        VBox.setMargin(infoLabel, new Insets(0, 0, 10, 20));

        getDialogPane().setContent(content);
        getDialogPane().setPrefWidth(550);

        setResultConverter(bouton -> {
            if (bouton == validerBtn) {
                return creerOuMettreAJourConsultation();
            }
            return null;
        });

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
        if (patientCombo.getValue() == null) return "Veuillez selectionner un patient.";
        if (medecinCombo.getValue() == null) return "Veuillez selectionner un medecin.";
        if (datePicker.getValue() == null) return "La date est obligatoire.";
        if (heureField.getText().trim().isEmpty()) return "L'heure est obligatoire.";
        try {
            LocalTime.parse(heureField.getText().trim());
        } catch (Exception e) {
            return "Format d'heure invalide. Utilisez HH:MM (ex: 14:30).";
        }
        if (motifField.getText().trim().isEmpty()) return "Le motif est obligatoire.";
        return null;
    }

    private Consultation creerOuMettreAJourConsultation() {
        Consultation c = (consultationExistante != null) ? consultationExistante : new Consultation();
        c.setPatient(patientCombo.getValue());
        c.setMedecin(medecinCombo.getValue());
        LocalTime heure = LocalTime.parse(heureField.getText().trim());
        c.setDateConsultation(LocalDateTime.of(datePicker.getValue(), heure));
        c.setMotif(motifField.getText().trim());
        c.setDiagnostic(diagnosticArea.getText().trim());
        c.setTraitement(traitementArea.getText().trim());
        return c;
    }
}