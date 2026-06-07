package com.medical.dao;

import com.medical.model.Patient;
import com.medical.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    /**
     * Récupère tous les patients.
     */
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY nom, prenom";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findAll patients : " + e.getMessage());
        }
        return patients;
    }

    /**
     * Trouve un patient par son ID.
     */
    public Patient findById(int id) {
        String sql = "SELECT * FROM patients WHERE id_patient = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPatient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur findById patient : " + e.getMessage());
        }
        return null;
    }

    /**
     * Recherche des patients par nom ou prénom.
     */
    public List<Patient> search(String keyword) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur search patients : " + e.getMessage());
        }
        return patients;
    }

    /**
     * Crée un nouveau patient.
     */
    public boolean create(Patient p) {
        String sql = "INSERT INTO patients (nom, prenom, date_naissance, sexe, telephone, email, adresse) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(p.getDateNaissance()));
            stmt.setString(4, p.getSexe());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getAdresse());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur create patient : " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un patient existant.
     */
    public boolean update(Patient p) {
        String sql = "UPDATE patients SET nom=?, prenom=?, date_naissance=?, sexe=?, telephone=?, email=?, adresse=? WHERE id_patient=?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(p.getDateNaissance()));
            stmt.setString(4, p.getSexe());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getAdresse());
            stmt.setInt(8, p.getIdPatient());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update patient : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un patient.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM patients WHERE id_patient = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete patient : " + e.getMessage());
            return false;
        }
    }

    /**
     * Compte le nombre total de patients (pour le dashboard).
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM patients";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur count patients : " + e.getMessage());
        }
        return 0;
    }

    /**
     * Convertit une ligne SQL en objet Patient.
     */
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setIdPatient(rs.getInt("id_patient"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        if (rs.getDate("date_naissance") != null) {
            p.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        }
        p.setSexe(rs.getString("sexe"));
        p.setTelephone(rs.getString("telephone"));
        p.setEmail(rs.getString("email"));
        p.setAdresse(rs.getString("adresse"));
        if (rs.getTimestamp("date_creation") != null) {
            p.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        }
        return p;
    }
}