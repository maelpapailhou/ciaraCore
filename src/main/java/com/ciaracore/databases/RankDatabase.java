package com.ciaracore.databases;

import com.ciaracore.managers.RankManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui gère la récupération et la mise à jour des ranks (grades) dans la base de données.
 */
public class RankDatabase {

    private final ConnectDatabase connectDatabase;

    public RankDatabase(ConnectDatabase connectDatabase) {
        this.connectDatabase = connectDatabase;
    }

    /**
     * Récupère tous les ranks disponibles dans la table des grades.
     * @return Liste des ranks sous forme d'objets Rank
     * @throws SQLException En cas d'erreur SQL
     */
    public List<RankManager.Rank> fetchAllRanks() throws SQLException {
        List<RankManager.Rank> ranks = new ArrayList<>();
        String query = "SELECT GRADE, PREFIX FROM grades";

        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String rankName = resultSet.getString("GRADE");
                String prefix = resultSet.getString("PREFIX");
                ranks.add(new RankManager.Rank(rankName, prefix));
            }
        }

        return ranks;
    }

    /**
     * Récupère le rank d'un joueur en fonction de son UUID.
     * @param playerUUID L'UUID du joueur
     * @return Nom du rank ou null si introuvable
     * @throws SQLException En cas d'erreur SQL
     */
    public String getPlayerRank(String playerUUID) throws SQLException {
        String query = "SELECT GRADE FROM players WHERE UUID = ?";
        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playerUUID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("GRADE");
                }
            }
        }
        return null;
    }

    /**
     * Définit un rank spécifique pour un joueur.
     * @param playerUUID L'UUID du joueur
     * @param rankName Le nom du rank à attribuer
     * @throws SQLException En cas d'erreur SQL
     */
    public void setPlayerRank(String playerUUID, String rankName) throws SQLException {
        String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, rankName);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        }
    }
}
