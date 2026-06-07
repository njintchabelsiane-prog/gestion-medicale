package com.medical.dao;

import com.medical.model.Medecin;
import com.medical.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    public List<Medecin> findAll() {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM medecins ORDER BY nom, prenom";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                medecins.add(mapResultSetToMedecin(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findAll medecins : " + e.getMessage());
        }
        return medecins;
    }

    public Medecin findById(int id) {
        String sql = "SELECT * FROM medecins WHERE id_medecin = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToMedecin(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById medecin : " + e.getMessage());
        }
        return null;
    }

    public boolean create(Medecin m) {
        String sql = "INSERT INTO medecins (nom, prenom, specialite, telephone, email, id_utilisateur) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNom());
            stmt.setString(2, m.getPrenom());
            stmt.setString(3, m.getSpecialite());
            stmt.setString(4, m.getTelephone());
            stmt.setString(5, m.getEmail());
            if (m.getIdUtilisateur() != null) {
                stmt.setInt(6, m.getIdUtilisateur());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur create medecin : " + e.getMessage());
            return false;
        }
    }

    public boolean update(Medecin m) {
        String sql = "UPDATE medecins SET nom=?, prenom=?, specialite=?, telephone=?, email=? WHERE id_medecin=?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNom());
            stmt.setString(2, m.getPrenom());
            stmt.setString(3, m.getSpecialite());
            stmt.setString(4, m.getTelephone());
            stmt.setString(5, m.getEmail());
            stmt.setInt(6, m.getIdMedecin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update medecin : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM medecins WHERE id_medecin = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete medecin : " + e.getMessage());
            return false;
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM medecins";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur count medecins : " + e.getMessage());
        }
        return 0;
    }

    private Medecin mapResultSetToMedecin(ResultSet rs) throws SQLException {
        Medecin m = new Medecin();
        m.setIdMedecin(rs.getInt("id_medecin"));
        m.setNom(rs.getString("nom"));
        m.setPrenom(rs.getString("prenom"));
        m.setSpecialite(rs.getString("specialite"));
        m.setTelephone(rs.getString("telephone"));
        m.setEmail(rs.getString("email"));
        int idUser = rs.getInt("id_utilisateur");
        m.setIdUtilisateur(rs.wasNull() ? null : idUser);
        return m;
    }
}