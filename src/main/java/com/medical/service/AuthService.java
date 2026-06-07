package com.medical.service;

import com.medical.dao.UtilisateurDAO;
import com.medical.model.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private static Utilisateur utilisateurConnecte = null;

    /**
     * Tente de connecter un utilisateur.
     * @return l'utilisateur si succès, null sinon
     */
    public Utilisateur login(String email, String motDePasse) {
        Utilisateur user = utilisateurDAO.findByEmail(email);
        if (user == null) {
            System.out.println("Utilisateur non trouvé.");
            return null;
        }

        // Vérifie le mot de passe avec BCrypt
        if (BCrypt.checkpw(motDePasse, user.getMotDePasse())) {
            utilisateurConnecte = user;
            System.out.println("Connexion réussie : " + user);
            return user;
        }

        System.out.println("Mot de passe incorrect.");
        return null;
    }

    /**
     * Hache un mot de passe en clair avec BCrypt.
     */
    public static String hashMotDePasse(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt(10));
    }

    /**
     * Déconnecte l'utilisateur actuel.
     */
    public void logout() {
        utilisateurConnecte = null;
    }

    /**
     * Retourne l'utilisateur actuellement connecté.
     */
    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * Vérifie si un utilisateur est connecté.
     */
    public static boolean estConnecte() {
        return utilisateurConnecte != null;
    }

    /**
     * Vérifie si l'utilisateur connecté est administrateur.
     */
    public static boolean estAdmin() {
        return utilisateurConnecte != null && "ADMIN".equals(utilisateurConnecte.getRole());
    }
}