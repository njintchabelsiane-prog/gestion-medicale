package com.medical.model;

public class Medecin {

    private int idMedecin;
    private String nom;
    private String prenom;
    private String specialite;
    private String telephone;
    private String email;
    private Integer idUtilisateur; // peut être null

    // Constructeur vide
    public Medecin() {}

    // Constructeur complet
    public Medecin(int idMedecin, String nom, String prenom, String specialite,
                   String telephone, String email, Integer idUtilisateur) {
        this.idMedecin = idMedecin;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
        this.email = email;
        this.idUtilisateur = idUtilisateur;
    }

    // Getters et Setters
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(Integer idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    @Override
    public String toString() {
        return "Dr " + prenom + " " + nom + " - " + specialite;
    }
}