package com.ciaracore.listeners;

import com.ciaracore.databases.UUIDDatabase;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final UUIDDatabase uuidDatabase;

    public PlayerJoinListener(UUIDDatabase uuidDatabase) {
        this.uuidDatabase = uuidDatabase;
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
        UUID uuid = event.getConnection().getUniqueId();
        String username = event.getConnection().getName();
        uuidDatabase.loadPlayer(uuid, username);
    }
}
