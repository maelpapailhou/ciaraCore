package com.ciaracore;

import com.ciaracore.commands.LobbyCommand;
import com.ciaracore.commands.RankCommand;
import com.ciaracore.databases.ConnectDatabase;
import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.listeners.ChatListener;
import com.ciaracore.listeners.MOTDListener;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.InputStream;
import java.sql.Connection;

public final class CiaraCore extends Plugin implements Listener {

    private ConnectDatabase connectDatabase;
    private UUIDDatabase uuidDatabase;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        getLogger().info("Démarrage du plugin CiaraCore...");

        // Chargement du fichier de configuration ranks.yml depuis les ressources
        InputStream configFile = getResourceAsStream("ranks.yml");

        if (configFile != null) {
            getLogger().info("Le fichier de configuration ranks.yml a été chargé avec succès.");

            // Initialisation du gestionnaire de rangs
            rankManager = new RankManager(configFile);
            getLogger().info("Le gestionnaire de rangs a été initialisé avec succès.");

            // Connexion à la base de données
            connectDatabase = new ConnectDatabase();
            if (connectDatabase.isConnected()) {
                getLogger().info("Connexion à la base de données établie avec succès.");

                // Récupération de la connexion à la base de données
                Connection connection = connectDatabase.getConnection();

                // Initialisation de la base de données UUID
                uuidDatabase = new UUIDDatabase(connection, rankManager);
                getLogger().info("La base de données UUID a été initialisée avec succès.");

                // Enregistrement des commandes et des écouteurs
                registerCommands();
                registerListeners();

                // Enregistrement du ChatListener après l'initialisation des dépendances
                getProxy().getPluginManager().registerListener(this, new ChatListener(uuidDatabase, rankManager));
                getLogger().info("Le ChatListener a été enregistré avec succès.");
            } else {
                getLogger().severe("Impossible de se connecter à la base de données. Le plugin sera désactivé.");
                getProxy().getPluginManager().unregisterListeners(this);
                getProxy().getPluginManager().unregisterCommands(this);
                return;
            }

            getLogger().info("CiaraCore a été activé avec succès.");
        } else {
            getLogger().severe("Impossible de trouver ranks.yml dans les ressources.");
            getProxy().getPluginManager().unregisterListeners(this);
            getProxy().getPluginManager().unregisterCommands(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Arrêt du plugin CiaraCore...");

        // Déconnexion de la base de données lors de la désactivation du plugin
        if (connectDatabase != null) {
            connectDatabase.disconnectFromDatabase();
            getLogger().info("Déconnexion de la base de données.");
        }

        getLogger().info("CiaraCore a été désactivé.");
    }

    private void registerCommands() {
        // Enregistrement des commandes
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
        getProxy().getPluginManager().registerCommand(this, new RankCommand(uuidDatabase, rankManager));
        getLogger().info("Les commandes ont été enregistrées avec succès.");
    }

    private void registerListeners() {
        // Enregistrement des écouteurs
        getProxy().getPluginManager().registerListener(this, new MOTDListener());
        getLogger().info("Les écouteurs ont été enregistrés avec succès.");
    }
}
