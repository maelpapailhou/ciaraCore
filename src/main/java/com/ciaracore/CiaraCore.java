package com.ciaracore;

import com.ciaracore.commands.GradeCommand;
import com.ciaracore.commands.LookupCommand;
import com.ciaracore.databases.ConnectDatabase;
import com.ciaracore.databases.GradeDatabase;
import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.listeners.ChatListener;
import com.ciaracore.listeners.MOTDListener;
import com.ciaracore.listeners.PlayerJoinListener;
import com.ciaracore.managers.GradeManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CiaraCore extends Plugin {

    private ConnectDatabase connectDatabase;
    private UUIDDatabase uuidDatabase;
    private GradeManager gradeManager;
    private ScheduledExecutorService databaseMonitor;

    @Override
    public void onEnable() {
        getLogger().info("[CiaraCore] Démarrage du plugin...");

        // Initialisation de la base de données
        connectDatabase = new ConnectDatabase();

        if (connectDatabase == null || !connectDatabase.isConnected()) {
            getLogger().severe("[CiaraCore] Impossible de se connecter à la base de données. Arrêt du plugin...");
            return;
        }

        // Initialisation des bases de données et gestionnaires
        uuidDatabase = new UUIDDatabase(connectDatabase);
        GradeDatabase gradeDatabase = new GradeDatabase(connectDatabase);
        gradeManager = new GradeManager(gradeDatabase);

        getLogger().info("[CiaraCore] Base de données initialisée et gestionnaires configurés.");

        // Enregistrement des commandes
        registerCommands();

        // Enregistrement des écouteurs
        registerListeners();

        // Démarrer un moniteur périodique pour surveiller la base de données
        startDatabaseMonitor();

        getLogger().info("[CiaraCore] Plugin activé avec succès.");
    }

    private void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new GradeCommand(uuidDatabase, gradeManager));
        getProxy().getPluginManager().registerCommand(this, new LookupCommand(uuidDatabase, gradeManager));
        getLogger().info("[CiaraCore] Commandes enregistrées.");
    }

    private void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(uuidDatabase));
        getProxy().getPluginManager().registerListener(this, new ChatListener(uuidDatabase, gradeManager));
        getProxy().getPluginManager().registerListener(this, new MOTDListener()); // Ajout ici
        getLogger().info("[CiaraCore] Écouteurs enregistrés.");
    }

    private void startDatabaseMonitor() {
        databaseMonitor = Executors.newSingleThreadScheduledExecutor();
        databaseMonitor.scheduleAtFixedRate(() -> {
            if (!connectDatabase.isConnected()) {
                getLogger().warning("[CiaraCore] Perte de connexion avec la base de données, tentative de reconnexion...");
                connectDatabase.disconnect(); // Assurez-vous de nettoyer l'ancienne connexion
                connectDatabase = new ConnectDatabase(); // Réinitialisez la connexion
                if (connectDatabase.isConnected()) {
                    getLogger().info("[CiaraCore] Reconnexion à la base de données réussie.");
                } else {
                    getLogger().severe("[CiaraCore] Impossible de se reconnecter à la base de données.");
                }
            }
        }, 0, 1, TimeUnit.MINUTES); // Vérification toutes les minutes
    }

    @Override
    public void onDisable() {
        if (databaseMonitor != null && !databaseMonitor.isShutdown()) {
            databaseMonitor.shutdown();
        }

        if (connectDatabase != null) {
            connectDatabase.disconnect();
        }
        getLogger().info("[CiaraCore] Plugin désactivé.");
    }
}
