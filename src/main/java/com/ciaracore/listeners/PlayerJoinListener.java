package com.ciaracore.listeners;

import com.ciaracore.databases.UUIDDatabase;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private UUIDDatabase uuidDatabase;

    public PlayerJoinListener(UUIDDatabase uuidDatabase) {
        this.uuidDatabase = uuidDatabase;
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();
        UUID uuid = connection.getUniqueId();
        String username = connection.getName();

        if (!uuidDatabase.playerExists(uuid)) {
            // Si le joueur n'existe pas dans la base de données, le créer
            uuidDatabase.loadPlayer(uuid, username);
        } else {
            // Vérifier si les informations du joueur sont à jour
            uuidDatabase.loadPlayer(uuid, username);
        }
    }
}
