package com.medical.controller;

import com.medical.model.Medecin;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MedecinFormDialog extends Dialog<Medecin> {

    private final TextField nomField = new TextField();
    private final TextField prenomField = new TextField();
    private final TextField specialiteField = new TextField();
    private final TextField telephoneField = new TextField();
    private final TextField emailField = new TextField();

    private final Medecin medecinExistant;

    public MedecinFormDialog(Medecin medecin) {
        this.medecinExistant = medecin;

        setTitle(medecin == null ? "Nouveau médecin" : "Modifier médecin");
        setHeaderText(medecin == null ? "Ajouter un nouveau médecin" : "Modifier les informations du médecin");

        ButtonType validerBtn = new ButtonType(medecin == null ? "Ajouter" : "Modifier", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(validerBtn, ButtonType.CANCEL);

        nomField.setPromptText("Ex: Dupont");
        prenomField.setPromptText("Ex: Marie");
        specialiteField.setPromptText("Ex: Médecine générale");
        telephoneField.setPromptText("Ex: 01 23 45 67 89");
        emailField.setPromptText("Ex: medecin@hopital.fr");

        if (medecin != null) {
            nomField.setText(medecin.getNom());
            prenomField.setText(medecin.getPrenom());
            specialiteField.setText(medecin.getSpecialite() != null ? medecin.getSpecialite() : "");
            telephoneField.setText(medecin.getTelephone() != null ? medecin.getTelephone() : "");
            emailField.setText(medecin.getEmail() != null ? medecin.getEmail() : "");
        }

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 20, 10, 20));

        grid.add(new Label("Nom *"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom *"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Spécialité *"), 0, 2);
        grid.add(specialiteField, 1, 2);
        grid.add(new Label("Téléphone"), 0, 3);
        grid.add(telephoneField, 1, 3);
        grid.add(new Label("Email"), 0, 4);
        grid.add(emailField, 1, 4);

        Label infoLabel = new Label("* Champs obligatoires");
        infoLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        VBox content = new VBox(10, grid, infoLabel);
        VBox.setMargin(infoLabel, new Insets(0, 0, 10, 20));

        getDialogPane().setContent(content);
        getDialogPane().setPrefWidth(500);

        setResultConverter(bouton -> {
            if (bouton == validerBtn) {
                return creerOuMettreAJourMedecin();
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
        if (nomField.getText().trim().isEmpty()) return "Le nom est obligatoire.";
        if (prenomField.getText().trim().isEmpty()) return "Le prénom est obligatoire.";
        if (specialiteField.getText().trim().isEmpty()) return "La spécialité est obligatoire.";
        return null;
    }

    private Medecin creerOuMettreAJourMedecin() {
        Medecin m = (medecinExistant != null) ? medecinExistant : new Medecin();
        m.setNom(nomField.getText().trim());
        m.setPrenom(prenomField.getText().trim());
        m.setSpecialite(specialiteField.getText().trim());
        m.setTelephone(telephoneField.getText().trim());
        m.setEmail(emailField.getText().trim());
        return m;
    }
}