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
        System.out.println("UsernameDatabase: Constructeur appelé, connexion établie.");
    }

    public UUID getPlayerUUID(String playerName) {
        System.out.println("UsernameDatabase: getPlayerUUID appelé pour le nom d'utilisateur: " + playerName);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM players WHERE USERNAME = ?");
            statement.setString(1, playerName);
            System.out.println("UsernameDatabase: Exécution de la requête SQL pour obtenir l'UUID.");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                System.out.println("UsernameDatabase: UUID trouvé: " + uuid);
                return uuid;
            } else {
                System.out.println("UsernameDatabase: Aucun UUID trouvé pour le nom d'utilisateur: " + playerName);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'UUID du joueur. Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerName(UUID uuid) {
        System.out.println("UsernameDatabase: getPlayerName appelé pour UUID: " + uuid);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT USERNAME FROM players WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            System.out.println("UsernameDatabase: Exécution de la requête SQL pour obtenir le nom d'utilisateur.");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("USERNAME");
                System.out.println("UsernameDatabase: Nom d'utilisateur trouvé: " + username);
                return username;
            } else {
                System.out.println("UsernameDatabase: Aucun nom d'utilisateur trouvé pour UUID: " + uuid);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nom du joueur. Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
