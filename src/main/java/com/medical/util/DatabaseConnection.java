package com.medical.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer la connexion à la base de données MySQL.
 * Pattern Singleton : une seule instance de connexion partagée.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_medicale";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    // Constructeur privé pour empêcher l'instanciation
    private DatabaseConnection() {}

    /**
     * Retourne la connexion à la base de données.
     * Crée la connexion si elle n'existe pas encore.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base de données réussie !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
}