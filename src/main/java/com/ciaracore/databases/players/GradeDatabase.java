package com.ciaracore.databases.players;

import com.ciaracore.databases.ConnectDatabase;
import com.ciaracore.managers.GradeManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui gère la récupération et la mise à jour des grades dans la base de données.
 */
public class GradeDatabase {

    private final ConnectDatabase connectDatabase;

    public GradeDatabase(ConnectDatabase connectDatabase) {
        this.connectDatabase = connectDatabase;
    }

    /**
     * Récupère tous les grades disponibles dans la table des grades.
     * @return Liste des grades sous forme d'objets Grade
     * @throws SQLException En cas d'erreur SQL
     */
    public List<GradeManager.Grade> fetchAllGrades() throws SQLException {
        List<GradeManager.Grade> grades = new ArrayList<>();
        // On sélectionne GRADE et PREFIX (et non NAME, pour correspondre à ta table)
        String query = "SELECT GRADE, PREFIX FROM grades";

        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Récupération du nom du grade dans la colonne "GRADE"
                String gradeName = resultSet.getString("GRADE");
                String prefix = resultSet.getString("PREFIX");
                grades.add(new GradeManager.Grade(gradeName, prefix));
            }
        }

        return grades;
    }

    /**
     * Récupère le grade d'un joueur en fonction de son UUID.
     * @param playerUUID L'UUID du joueur
     * @return Nom du grade ou null si introuvable
     * @throws SQLException En cas d'erreur SQL
     */
    public String getPlayerGrade(String playerUUID) throws SQLException {
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
        // Aucun grade trouvé
        return null;
    }

    /**
     * Définit un grade spécifique pour un joueur.
     * @param playerUUID L'UUID du joueur
     * @param gradeName Le nom du grade à attribuer
     * @throws SQLException En cas d'erreur SQL
     */
    public void setPlayerGrade(String playerUUID, String gradeName) throws SQLException {
        String query = "UPDATE players SET GRADE = ? WHERE UUID = ?";
        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, gradeName);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        }
    }
}
