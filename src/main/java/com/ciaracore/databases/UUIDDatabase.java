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
    private RankManager rankManager;

    public UUIDDatabase(Connection connection, RankManager rankManager) {
        this.connection = connection;
        this.rankManager = rankManager;
        this.rankDatabase = new RankDatabase(connection);
        this.usernameDatabase = new UsernameDatabase(connection);
    }

    public void loadPlayer(UUID uuid, String username) {
        try {
            System.out.println("Loading player with UUID: " + uuid.toString() + " and username: " + username);

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Player not found, adding to the database.");

                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO players (UUID, USERNAME, `RANK`, COINS) VALUES (?, ?, 'guest', 0)");
                insertStatement.setString(1, uuid.toString());
                insertStatement.setString(2, username);
                insertStatement.executeUpdate();
                insertStatement.close();
            } else {
                System.out.println("Player found in the database.");
            }

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE players SET USERNAME = ? WHERE UUID = ?");
            updateStatement.setString(1, username);
            updateStatement.setString(2, uuid.toString());
            updateStatement.executeUpdate();
            updateStatement.close();

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error loading player data. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getPlayerRank(UUID uuid) {
        return rankDatabase.getPlayerRank(uuid);
    }

    public UUID getPlayerUUID(String playerName) {
        return usernameDatabase.getPlayerUUID(playerName);
    }

    public String getPlayerName(UUID uuid) {
        return usernameDatabase.getPlayerName(uuid);
    }

    public void setPlayerRank(UUID playerUUID, String rankName) {
        rankDatabase.setPlayerRank(playerUUID, rankName);
    }
}
