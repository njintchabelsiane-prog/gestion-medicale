package com.medical.dao;

import com.medical.model.Utilisateur;
import com.medical.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    /**
     * Trouve un utilisateur par son email (pour le login).
     */
    public Utilisateur findByEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByEmail : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY nom, prenom";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findAll : " + e.getMessage());
        }
        return utilisateurs;
    }

    /**
     * Crée un nouvel utilisateur.
     */
    public boolean create(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur create : " + e.getMessage());
            return false;
        }
    }

    /**
     * Convertit une ligne SQL en objet Utilisateur.
     */
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getInt("id_utilisateur"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRole(rs.getString("role"));
        if (rs.getTimestamp("date_creation") != null) {
            u.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        }
        return u;
    }
}