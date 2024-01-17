package com.ciaracore.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RankDatabase {

    private Connection connection;

    public RankDatabase(Connection connection) {
        this.connection = connection;
    }

    public String getPlayerRank(UUID playerUUID) {
        String rank = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `RANK` FROM players WHERE UUID = ?");
            statement.setString(1, playerUUID.toString());
            rank = executeSingleStringQuery(statement);
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return rank;
    }

    public void setPlayerRank(UUID playerUUID, String newRank) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE players SET `RANK` = ? WHERE UUID = ?")) {
            statement.setString(1, newRank);
            statement.setString(2, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private String executeSingleStringQuery(PreparedStatement statement) throws SQLException {
        String result = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        }
        return result;
    }

    private void handleSQLException(SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}
