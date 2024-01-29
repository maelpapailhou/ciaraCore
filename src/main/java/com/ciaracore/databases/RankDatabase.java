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
        System.out.println("RankDatabase: Constructeur appelé, connexion établie.");
    }

    public String getPlayerRank(UUID playerUUID) {
        System.out.println("RankDatabase: getPlayerRank appelé pour UUID: " + playerUUID);
        String rank = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `RANK` FROM players WHERE UUID = ?");
            statement.setString(1, playerUUID.toString());
            System.out.println("RankDatabase: Requête préparée exécutée.");
            rank = executeSingleStringQuery(statement);
        } catch (SQLException e) {
            handleSQLException(e);
        }

        if (rank != null) {
            System.out.println("RankDatabase: Rang trouvé: " + rank);
        } else {
            System.out.println("RankDatabase: Aucun rang trouvé pour UUID: " + playerUUID);
        }
        return rank;
    }

    public void setPlayerRank(UUID playerUUID, String newRank) {
        System.out.println("RankDatabase: setPlayerRank appelé pour UUID: " + playerUUID + ", Rang: " + newRank);
        try (PreparedStatement statement = connection.prepareStatement("UPDATE players SET `RANK` = ? WHERE UUID = ?")) {
            statement.setString(1, newRank);
            statement.setString(2, playerUUID.toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("RankDatabase: Mise à jour du rang réussie.");
            } else {
                System.out.println("RankDatabase: Aucune ligne affectée, mise à jour du rang échouée.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private String executeSingleStringQuery(PreparedStatement statement) throws SQLException {
        String result = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getString(1);
                System.out.println("RankDatabase: Résultat de la requête: " + result);
            } else {
                System.out.println("RankDatabase: Aucun résultat trouvé pour la requête.");
            }
        }
        return result;
    }

    private void handleSQLException(SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}
