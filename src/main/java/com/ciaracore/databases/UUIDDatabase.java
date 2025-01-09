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

    public boolean playerExists(UUID uuid) {
        String query = "SELECT COUNT(*) FROM players WHERE uuid = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la vérification de l'existence du joueur : " + e.getMessage());
            return false;
        }
    }

    public void loadPlayer(UUID uuid, String username) {
        if (!playerExists(uuid)) {
            String query = "INSERT INTO players (uuid, username, grade) VALUES (?, ?, 'default')";
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
     * Récupère le grade d’un joueur en fonction de son UUID (surcharge).
     * @param uuid UUID du joueur
     * @return Nom du grade, ou null si introuvable
     */
    public String getPlayerGrade(UUID uuid) {
        try {
            String query = "SELECT GRADE FROM players WHERE UUID = ?";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, uuid.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("GRADE");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération du grade du joueur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère le grade d’un joueur en fonction de son nom (surcharge).
     * @param playerName Nom du joueur
     * @return Nom du grade, ou null si introuvable
     */
    public String getPlayerGrade(String playerName) {
        UUID uuid = getPlayerUUID(playerName);
        return (uuid != null) ? getPlayerGrade(uuid) : null;
    }

    /**
     * Récupère l'UUID d’un joueur en fonction de son nom.
     * @param playerName Nom du joueur
     * @return UUID du joueur, ou null si introuvable
     */
    public UUID getPlayerUUID(String playerName) {
        try {
            String query = "SELECT UUID FROM players WHERE USERNAME = ?";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, playerName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return UUID.fromString(rs.getString("UUID"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la récupération de l'UUID du joueur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère le nom d’un joueur en fonction de son UUID.
     * @param uuid UUID du joueur
     * @return Nom du joueur, ou null si introuvable
     */
    public String getPlayerName(UUID uuid) {
        try {
            String query = "SELECT USERNAME FROM players WHERE UUID = ?";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, uuid.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("USERNAME");
                    }
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
        try {
            String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, gradeName);
                pstmt.setString(2, uuid.toString());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Erreur lors de la mise à jour du grade du joueur : " + e.getMessage());
        }
    }
}
