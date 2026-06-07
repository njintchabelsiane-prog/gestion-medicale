package com.medical.dao;

import com.medical.model.Consultation;
import com.medical.model.Medecin;
import com.medical.model.Patient;
import com.medical.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    private static final String SELECT_JOIN =
        "SELECT c.*, " +
        "p.nom AS p_nom, p.prenom AS p_prenom, p.date_naissance AS p_dn, p.sexe AS p_sexe, " +
        "p.telephone AS p_tel, p.email AS p_email, p.adresse AS p_adresse, " +
        "m.nom AS m_nom, m.prenom AS m_prenom, m.specialite AS m_specialite, " +
        "m.telephone AS m_tel, m.email AS m_email " +
        "FROM consultations c " +
        "LEFT JOIN patients p ON c.id_patient = p.id_patient " +
        "LEFT JOIN medecins m ON c.id_medecin = m.id_medecin ";

    public List<Consultation> findAll() {
        List<Consultation> consultations = new ArrayList<>();
        String sql = SELECT_JOIN + "ORDER BY c.date_consultation DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findAll consultations : " + e.getMessage());
        }
        return consultations;
    }

    public Consultation findById(int id) {
        String sql = SELECT_JOIN + "WHERE c.id_consultation = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToConsultation(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById consultation : " + e.getMessage());
        }
        return null;
    }

    public List<Consultation> findByPatient(int idPatient) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = SELECT_JOIN + "WHERE c.id_patient = ? ORDER BY c.date_consultation DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByPatient : " + e.getMessage());
        }
        return consultations;
    }

    public List<Consultation> findRecent(int limit) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = SELECT_JOIN + "ORDER BY c.date_consultation DESC LIMIT ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findRecent : " + e.getMessage());
        }
        return consultations;
    }

    public boolean create(Consultation c) {
        String sql = "INSERT INTO consultations (id_patient, id_medecin, date_consultation, motif, diagnostic, traitement) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdPatient());
            stmt.setInt(2, c.getIdMedecin());
            stmt.setTimestamp(3, Timestamp.valueOf(c.getDateConsultation()));
            stmt.setString(4, c.getMotif());
            stmt.setString(5, c.getDiagnostic());
            stmt.setString(6, c.getTraitement());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur create consultation : " + e.getMessage());
            return false;
        }
    }

    public boolean update(Consultation c) {
        String sql = "UPDATE consultations SET id_patient=?, id_medecin=?, date_consultation=?, motif=?, diagnostic=?, traitement=? WHERE id_consultation=?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdPatient());
            stmt.setInt(2, c.getIdMedecin());
            stmt.setTimestamp(3, Timestamp.valueOf(c.getDateConsultation()));
            stmt.setString(4, c.getMotif());
            stmt.setString(5, c.getDiagnostic());
            stmt.setString(6, c.getTraitement());
            stmt.setInt(7, c.getIdConsultation());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update consultation : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM consultations WHERE id_consultation = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete consultation : " + e.getMessage());
            return false;
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM consultations";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur count consultations : " + e.getMessage());
        }
        return 0;
    }

    public int countToday() {
        String sql = "SELECT COUNT(*) FROM consultations WHERE DATE(date_consultation) = CURDATE()";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur countToday : " + e.getMessage());
        }
        return 0;
    }

    private Consultation mapResultSetToConsultation(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setIdConsultation(rs.getInt("id_consultation"));
        c.setIdPatient(rs.getInt("id_patient"));
        c.setIdMedecin(rs.getInt("id_medecin"));
        if (rs.getTimestamp("date_consultation") != null) {
            c.setDateConsultation(rs.getTimestamp("date_consultation").toLocalDateTime());
        }
        c.setMotif(rs.getString("motif"));
        c.setDiagnostic(rs.getString("diagnostic"));
        c.setTraitement(rs.getString("traitement"));

        // Charger le patient associe
        Patient p = new Patient();
        p.setIdPatient(rs.getInt("id_patient"));
        p.setNom(rs.getString("p_nom"));
        p.setPrenom(rs.getString("p_prenom"));
        if (rs.getDate("p_dn") != null) p.setDateNaissance(rs.getDate("p_dn").toLocalDate());
        p.setSexe(rs.getString("p_sexe"));
        p.setTelephone(rs.getString("p_tel"));
        p.setEmail(rs.getString("p_email"));
        p.setAdresse(rs.getString("p_adresse"));
        c.setPatient(p);

        // Charger le medecin associe
        Medecin m = new Medecin();
        m.setIdMedecin(rs.getInt("id_medecin"));
        m.setNom(rs.getString("m_nom"));
        m.setPrenom(rs.getString("m_prenom"));
        m.setSpecialite(rs.getString("m_specialite"));
        m.setTelephone(rs.getString("m_tel"));
        m.setEmail(rs.getString("m_email"));
        c.setMedecin(m);

        return c;
    }
}