package com.ciaracore.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsernameDatabase {

    private Connection connection;

    public UsernameDatabase(Connection connection) {
        this.connection = connection;
    }

    public UUID getPlayerUUID(String playerName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM players WHERE USERNAME = ?");
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString("UUID"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting player UUID. Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerName(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT USERNAME FROM players WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("USERNAME");
            }
        } catch (SQLException e) {
            System.err.println("Error getting player name. Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
