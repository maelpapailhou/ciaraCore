package com.ciaracore.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDDatabase {
    private final ConnectDatabase connectDatabase;

    public UUIDDatabase(ConnectDatabase connectDatabase) {
        this.connectDatabase = connectDatabase;
    }

    /**
     * Récupère une option spécifique pour un joueur dans la table players_options.
     * @param uuid UUID du joueur
     * @param optionName Nom de l'option (colonne dans la table)
     * @return Valeur de l'option, ou null si non définie
     */
    public String getPlayerOption(UUID uuid, String optionName) {
        String query = "SELECT " + optionName + " FROM players_options WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(optionName);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération de l'option " + optionName + " : " + e.getMessage());
        }
        return null;
    }

    /**
     * Définit une option spécifique pour un joueur dans la table players_options.
     * @param uuid UUID du joueur
     * @param optionName Nom de l'option (colonne dans la table)
     * @param optionValue Valeur de l'option à définir
     */
    public void setPlayerOption(UUID uuid, String optionName, String optionValue) {
        String query = "INSERT INTO players_options (UUID, " + optionName + ") VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " + optionName + " = VALUES(" + optionName + ")";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, optionValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la mise à jour de l'option " + optionName + " : " + e.getMessage());
        }
    }


    /**
     * Vérifie si un joueur existe dans la base de données.
     * @param uuid UUID du joueur
     * @return true si le joueur existe, false sinon
     */
    public boolean playerExists(UUID uuid) {
        String query = "SELECT COUNT(*) FROM players WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la vérification de l'existence du joueur : " + e.getMessage());
            return false;
        }
    }

    /**
     * Charge un joueur dans la base de données s'il n'existe pas.
     * @param uuid UUID du joueur
     * @param username Nom du joueur
     */
    public void loadPlayer(UUID uuid, String username) {
        if (!playerExists(uuid)) {
            String query = "INSERT INTO players (UUID, USERNAME, GRADE) VALUES (?, ?, 'default')";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, uuid.toString());
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[UUIDDatabase] Erreur lors de l'ajout du joueur : " + e.getMessage());
            }
        }
    }

    /**
     * Récupère le grade d’un joueur à partir de son UUID.
     * @param uuid UUID du joueur
     * @return Nom du grade, ou null si introuvable
     */
    public String getPlayerGrade(UUID uuid) {
        String query = "SELECT GRADE FROM players WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("GRADE");
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération du grade du joueur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère le grade d’un joueur à partir de son nom.
     * @param playerName Nom du joueur
     * @return Nom du grade, ou null si introuvable
     */
    public String getPlayerGrade(String playerName) {
        UUID uuid = getPlayerUUID(playerName);
        return (uuid != null) ? getPlayerGrade(uuid) : null;
    }

    /**
     * Récupère l'UUID d’un joueur à partir de son nom.
     * @param playerName Nom du joueur
     * @return UUID du joueur, ou null si introuvable
     */
    public UUID getPlayerUUID(String playerName) {
        String query = "SELECT UUID FROM players WHERE USERNAME = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("UUID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération de l'UUID du joueur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère le nom d’un joueur à partir de son UUID.
     * @param uuid UUID du joueur
     * @return Nom du joueur, ou null si introuvable
     */
    public String getPlayerName(UUID uuid) {
        String query = "SELECT USERNAME FROM players WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération du nom du joueur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Définit le grade d’un joueur.
     * @param uuid UUID du joueur
     * @param gradeName Nom du grade
     */
    public void setPlayerGrade(UUID uuid, String gradeName) {
        String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gradeName);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la mise à jour du grade du joueur : " + e.getMessage());
        }
    }

    /**
     * Définit la langue d’un joueur.
     * @param uuid UUID du joueur
     * @param language Langue à définir
     */
    public void setPlayerLanguage(UUID uuid, String language) {
        String query = "INSERT INTO players_options (UUID, LANGUAGE) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE LANGUAGE = VALUES(LANGUAGE)";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, language);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la mise à jour de la langue du joueur : " + e.getMessage());
        }
    }

    /**
     * Récupère la langue d’un joueur.
     * @param uuid UUID du joueur
     * @return Langue du joueur, ou null si introuvable
     */
    public String getPlayerLanguage(UUID uuid) {
        String query = "SELECT LANGUAGE FROM players_options WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("LANGUAGE");
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération de la langue du joueur : " + e.getMessage());
        }
        return null;
    }
}
