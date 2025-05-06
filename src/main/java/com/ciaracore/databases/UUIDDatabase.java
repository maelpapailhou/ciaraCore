package com.ciaracore.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDDatabase {

    private final ConnectDatabase connectDatabase;

    // Constructor to initialize UUIDDatabase with the connection
    public UUIDDatabase(ConnectDatabase connectDatabase) {
        this.connectDatabase = connectDatabase;
    }

    /**
     * Retrieves a specific option for a player from the players_options table.
     * @param uuid UUID of the player
     * @param optionName Name of the option (column in the table)
     * @return Value of the option, or null if not defined
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
            System.err.println("[UUIDDatabase] Error while retrieving option " + optionName + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets a specific option for a player in the players_options table.
     * @param uuid UUID of the player
     * @param optionName Name of the option (column in the table)
     * @param optionValue Value of the option to set
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
            System.err.println("[UUIDDatabase] Error while updating option " + optionName + ": " + e.getMessage());
        }
    }

    /**
     * Checks if a player exists in the database.
     * @param uuid UUID of the player
     * @return true if the player exists, false otherwise
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
            System.err.println("[UUIDDatabase] Error while checking if player exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads a player into the database if they do not exist.
     * @param uuid UUID of the player
     * @param username Username of the player
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
                System.err.println("[UUIDDatabase] Error while adding player: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves the grade of a player by their UUID.
     * @param uuid UUID of the player
     * @return Grade of the player, or null if not found
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
            System.err.println("[UUIDDatabase] Error while retrieving player's grade: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the grade of a player by their name.
     * @param playerName Name of the player
     * @return Grade of the player, or null if not found
     */
    public String getPlayerGrade(String playerName) {
        UUID uuid = getPlayerUUID(playerName);
        return (uuid != null) ? getPlayerGrade(uuid) : null;
    }

    /**
     * Retrieves the UUID of a player by their name.
     * @param playerName Name of the player
     * @return UUID of the player, or null if not found
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
            System.err.println("[UUIDDatabase] Error while retrieving player's UUID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the name of a player by their UUID.
     * @param uuid UUID of the player
     * @return Name of the player, or null if not found
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
            System.err.println("[UUIDDatabase] Error while retrieving player's name: " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets the grade of a player.
     * @param uuid UUID of the player
     * @param gradeName Name of the grade
     */
    public void setPlayerGrade(UUID uuid, String gradeName) {
        String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gradeName);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[UUIDDatabase] Error while updating player's grade: " + e.getMessage());
        }
    }

    /**
     * Sets the language of a player.
     * @param uuid UUID of the player
     * @param language Language to set
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
            System.err.println("[UUIDDatabase] Error while updating player's language: " + e.getMessage());
        }
    }

    /**
     * Retrieves the language of a player.
     * @param uuid UUID of the player
     * @return Language of the player, or null if not found
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
            System.err.println("[UUIDDatabase] Error while retrieving player's language: " + e.getMessage());
        }
        return null;
    }
}
