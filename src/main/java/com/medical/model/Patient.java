package com.medical.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class Patient {

    private int idPatient;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe; // M ou F
    private String telephone;
    private String email;
    private String adresse;
    private LocalDateTime dateCreation;

    // Constructeur vide
    public Patient() {}

    // Constructeur complet
    public Patient(int idPatient, String nom, String prenom, LocalDate dateNaissance,
                   String sexe, String telephone, String email, String adresse,
                   LocalDateTime dateCreation) {
        this.idPatient = idPatient;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
    }

    // Méthode utilitaire : calcule l'âge automatiquement
    public int getAge() {
        if (dateNaissance == null) return 0;
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    // Getters et Setters
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + getAge() + " ans)";
    }
}