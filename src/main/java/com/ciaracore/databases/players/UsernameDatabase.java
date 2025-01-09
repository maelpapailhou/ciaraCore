package com.ciaracore.databases.players;

import com.ciaracore.databases.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsernameDatabase {

    private final ConnectDatabase connectDatabase;

    public UsernameDatabase(ConnectDatabase connectDatabase) {
        this.connectDatabase = connectDatabase;
        System.out.println("UsernameDatabase: Constructeur appelé (pool-based).");
    }

    public UUID getPlayerUUID(String playerName) {
        System.out.println("UsernameDatabase: getPlayerUUID appelé pour playerName: " + playerName);
        String query = "SELECT UUID FROM players WHERE USERNAME = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("UUID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerName(UUID uuid) {
        System.out.println("UsernameDatabase: getPlayerName appelé pour UUID: " + uuid);
        String query = "SELECT USERNAME FROM players WHERE UUID = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nom du joueur. Erreur: " + e.getMessage());
        }
        return null;
    }
}
