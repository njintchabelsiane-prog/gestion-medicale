package com.medical.model;

import java.time.LocalDateTime;

public class Consultation {

    private int idConsultation;
    private int idPatient;
    private int idMedecin;
    private LocalDateTime dateConsultation;
    private String motif;
    private String diagnostic;
    private String traitement;

    // Objets associes pour affichage
    private Patient patient;
    private Medecin medecin;

    public Consultation() {}

    public Consultation(int idConsultation, int idPatient, int idMedecin,
                        LocalDateTime dateConsultation, String motif,
                        String diagnostic, String traitement) {
        this.idConsultation = idConsultation;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
    }

    public int getIdConsultation() { return idConsultation; }
    public void setIdConsultation(int idConsultation) { this.idConsultation = idConsultation; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }

    public LocalDateTime getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(LocalDateTime dateConsultation) { this.dateConsultation = dateConsultation; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient != null) this.idPatient = patient.getIdPatient();
    }

    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
        if (medecin != null) this.idMedecin = medecin.getIdMedecin();
    }

    @Override
    public String toString() {
        return "Consultation du " + dateConsultation + " - Motif: " + motif;
    }
}