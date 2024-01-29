package com.ciaracore.databases;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDatabase {
    private Connection connection;

    public ConnectDatabase() {
        // Chargement des propriétés de la base de données depuis le fichier config.properties
        Properties properties = loadProperties();

        if (properties != null) {
            // Récupération des informations de connexion depuis les propriétés
            String host = properties.getProperty("db.host");
            String database = properties.getProperty("db.database");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            int port = Integer.parseInt(properties.getProperty("db.port"));

            // Connexion à la base de données
            connectToDatabase(host, database, username, password, port);
        } else {
            System.err.println("ConnectDatabase: Erreur lors du chargement des propriétés de la base de données. Le plugin sera désactivé.");
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                System.out.println("ConnectDatabase: Propriétés de la base de données chargées avec succès.");
            } else {
                System.err.println("ConnectDatabase: Désolé, impossible de trouver config.properties");
            }
        } catch (Exception e) {
            System.err.println("ConnectDatabase: Erreur lors du chargement des propriétés. Erreur : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    private void connectToDatabase(String host, String database, String username, String password, int port) {
        try {
            // Connexion à la base de données MySQL
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            System.out.println("ConnectDatabase: Connecté à la base de données.");
        } catch (SQLException e) {
            System.err.println("ConnectDatabase: Échec de la connexion à la base de données. Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void disconnectFromDatabase() {
        try {
            // Vérification de la connexion avant de se déconnecter
            if (isConnected()) {
                connection.close();
                System.out.println("ConnectDatabase: Déconnecté de la base de données.");
            }
        } catch (SQLException e) {
            System.err.println("ConnectDatabase: Erreur lors de la déconnexion de la base de données. Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
