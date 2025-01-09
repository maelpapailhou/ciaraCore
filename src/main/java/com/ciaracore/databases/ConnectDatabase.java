package com.ciaracore.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectDatabase {

    private HikariDataSource dataSource;

    public ConnectDatabase() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/ciaracube?useSSL=false"); // Changez les valeurs ici
            config.setUsername("root"); // Remplacez par votre utilisateur MySQL
            config.setPassword("password"); // Remplacez par votre mot de passe MySQL
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(10000);

            dataSource = new HikariDataSource(config);
            System.out.println("[ConnectDatabase] Connexion à la base de données réussie.");
        } catch (Exception e) {
            System.err.println("[ConnectDatabase] Erreur lors de l'initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (dataSource == null) {
                System.err.println("[ConnectDatabase] Le pool de connexions est null.");
                return null;
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("[ConnectDatabase] Erreur lors de l'obtention de la connexion : " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("[ConnectDatabase] Connexion à la base de données fermée.");
        }
    }
}
