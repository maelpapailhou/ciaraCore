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
        String query = "SELECT COUNT(*) FROM players WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Error checking player existence: " + e.getMessage());
        }
        return false;
    }

    public void loadPlayer(UUID uuid, String username) {
        if (!playerExists(uuid)) {
            String query = "INSERT INTO players (UUID, USERNAME, GRADE) VALUES (?, ?, 'default')";
            try (Connection conn = connectDatabase.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, uuid.toString());
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[UUIDDatabase] Error inserting new player: " + e.getMessage());
            }
        }
    }

    public String getPlayerRank(UUID uuid) {
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
            System.err.println("[UUIDDatabase] Error retrieving player rank: " + e.getMessage());
        }
        return null;
    }

    public String getPlayerRank(String playerName) {
        UUID uuid = getPlayerUUID(playerName);
        return (uuid != null) ? getPlayerRank(uuid) : null;
    }

    public void setPlayerRank(UUID uuid, String rankName) {
        String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, rankName);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Error updating player rank: " + e.getMessage());
        }
    }

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
            System.err.println("[UUIDDatabase] Error retrieving player's UUID: " + e.getMessage());
        }
        return null;
    }

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
            System.err.println("[UUIDDatabase] Error retrieving player's name: " + e.getMessage());
        }
        return null;
    }

    public String getPlayerLanguage(UUID uuid) {
        return getPlayerOption(uuid, "LANGUAGE");
    }

    public void setPlayerLanguage(UUID uuid, String language) {
        setPlayerOption(uuid, "LANGUAGE", language);
    }

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
            System.err.println("[UUIDDatabase] Error retrieving option '" + optionName + "': " + e.getMessage());
        }
        return null;
    }

    public void setPlayerOption(UUID uuid, String optionName, String optionValue) {
        String query = "INSERT INTO players_options (UUID, " + optionName + ") VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " + optionName + " = VALUES(" + optionName + ")";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, optionValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Error updating option '" + optionName + "': " + e.getMessage());
        }
    }
}
