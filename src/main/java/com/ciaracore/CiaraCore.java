package com.ciaracore;

import com.ciaracore.commands.LobbyCommand;
import com.ciaracore.commands.IDCommand;
import com.ciaracore.commands.RankSetCommand;
import com.ciaracore.databases.ConnectDatabase;
import com.ciaracore.databases.RankDatabase;
import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.databases.UsernameDatabase;
import com.ciaracore.listeners.ChatListener;
import com.ciaracore.listeners.MOTDListener;
import com.ciaracore.listeners.PlayerJoinListener;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.InputStream;
import java.sql.Connection;

public final class CiaraCore extends Plugin implements Listener {

    private ConnectDatabase connectDatabase;
    private UUIDDatabase uuidDatabase;
    private RankDatabase rankDatabase;
    private UsernameDatabase usernameDatabase;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        getLogger().info("Démarrage du plugin CiaraCore...");

        // Chargement du fichier de configuration ranks.yml depuis les ressources
        InputStream configFile = getResourceAsStream("ranks.yml");
        if (configFile == null) {
            getLogger().severe("Impossible de trouver ranks.yml dans les ressources.");
            disablePlugin();
            return;
        }
        getLogger().info("Le fichier de configuration ranks.yml a été chargé avec succès.");

        // Initialisation du gestionnaire de rangs
        rankManager = new RankManager(configFile);
        getLogger().info("Gestionnaire de rangs initialisé.");

        // Connexion à la base de données
        connectDatabase = new ConnectDatabase();
        if (!connectDatabase.isConnected()) {
            getLogger().severe("Impossible de se connecter à la base de données. Le plugin sera désactivé.");
            disablePlugin();
            return;
        }
        getLogger().info("Connexion à la base de données réussie.");

        Connection connection = connectDatabase.getConnection();

        // Initialisation des bases de données
        rankDatabase = new RankDatabase(connection);
        uuidDatabase = new UUIDDatabase(connection);
        usernameDatabase = new UsernameDatabase(connection);
        getLogger().info("Bases de données initialisées.");

        // Enregistrement des commandes et des écouteurs
        registerCommands();
        registerListeners();

        // Enregistrement des écouteurs spécifiques
        getProxy().getPluginManager().registerListener(this, new ChatListener(uuidDatabase, rankManager));
        getLogger().info("Écouteurs enregistrés.");

        getLogger().info("CiaraCore activé avec succès.");
    }

    private void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
        getProxy().getPluginManager().registerCommand(this, new RankSetCommand(uuidDatabase, rankManager));
        getProxy().getPluginManager().registerCommand(this, new IDCommand(uuidDatabase, rankManager));
        getLogger().info("Commandes enregistrées.");
    }

    private void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new MOTDListener());
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(uuidDatabase));
        getLogger().info("Écouteurs enregistrés.");
    }

    private void disablePlugin() {
        getProxy().getPluginManager().unregisterListeners(this);
        getProxy().getPluginManager().unregisterCommands(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Arrêt du plugin CiaraCore...");
        if (connectDatabase != null) {
            connectDatabase.disconnectFromDatabase();
            getLogger().info("Déconnecté de la base de données.");
        }
        getLogger().info("CiaraCore désactivé.");
    }
}
