package com.ciaracore.databases;

import com.ciaracore.managers.RankManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDDatabase {

    private Connection connection;
    private RankDatabase rankDatabase;
    private UsernameDatabase usernameDatabase;

    public UUIDDatabase(Connection connection) {
        this.connection = connection;
        this.rankDatabase = new RankDatabase(connection);
        this.usernameDatabase = new UsernameDatabase(connection);
        System.out.println("UUIDDatabase initialized.");
    }

    public void loadPlayer(UUID uuid, String username) {
        System.out.println("UUIDDatabase: Loading player with UUID: " + uuid + " and username: " + username);
        try {
            // Check if player exists in the database
            PreparedStatement checkStatement = connection.prepareStatement("SELECT USERNAME FROM players WHERE UUID = ?");
            checkStatement.setString(1, uuid.toString());
            ResultSet resultSet = checkStatement.executeQuery();

            if (!resultSet.next()) {
                // Player not found, add new player to the database
                System.out.println("UUIDDatabase: Player not found in the database. Adding new player entry.");
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO players (UUID, USERNAME, `RANK`, COINS) VALUES (?, ?, 'guest', 0)");
                insertStatement.setString(1, uuid.toString());
                insertStatement.setString(2, username);
                insertStatement.executeUpdate();
                insertStatement.close();
            } else {
                // Player found, update username if necessary
                String existingUsername = resultSet.getString("USERNAME");
                if (!existingUsername.equals(username)) {
                    System.out.println("UUIDDatabase: Username has changed. Updating username in the database.");
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE players SET USERNAME = ? WHERE UUID = ?");
                    updateStatement.setString(1, username);
                    updateStatement.setString(2, uuid.toString());
                    updateStatement.executeUpdate();
                    updateStatement.close();
                } else {
                    System.out.println("UUIDDatabase: Username is the same. No update needed.");
                }
            }

            resultSet.close();
            checkStatement.close();
        } catch (SQLException e) {
            System.err.println("UUIDDatabase: Error loading player data. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getPlayerRank(UUID uuid) {
        System.out.println("UUIDDatabase: Retrieving rank for UUID: " + uuid);
        return rankDatabase.getPlayerRank(uuid);
    }

    public UUID getPlayerUUID(String playerName) {
        System.out.println("UUIDDatabase: Retrieving UUID for player name: " + playerName);
        return usernameDatabase.getPlayerUUID(playerName);
    }

    public String getPlayerName(UUID uuid) {
        System.out.println("UUIDDatabase: Retrieving player name for UUID: " + uuid);
        return usernameDatabase.getPlayerName(uuid);
    }

    public void setPlayerRank(UUID playerUUID, String rankName) {
        System.out.println("UUIDDatabase: Setting rank for UUID: " + playerUUID + " to " + rankName);
        rankDatabase.setPlayerRank(playerUUID, rankName);
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM players WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            statement.close();
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("UUIDDatabase: Error checking if player exists. Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getPlayerCoins(UUID uuid) {
        System.out.println("UUIDDatabase: Retrieving coins for UUID: " + uuid);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COINS FROM players WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int coins = resultSet.getInt("COINS");
                statement.close();
                resultSet.close();
                return coins;
            } else {
                statement.close();
                resultSet.close();
                return 0; // ou une autre valeur par défaut si le joueur n'est pas trouvé
            }
        } catch (SQLException e) {
            System.err.println("UUIDDatabase: Error retrieving player coins. Error: " + e.getMessage());
            e.printStackTrace();
            return 0; // ou une autre valeur par défaut en cas d'erreur
        }
    }
}
