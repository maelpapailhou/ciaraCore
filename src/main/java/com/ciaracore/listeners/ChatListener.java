package com.ciaracore.listeners;

import com.ciaracore.databases.players.UUIDDatabase;
import com.ciaracore.managers.GradeManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ChatListener implements Listener {

    private final UUIDDatabase uuidDatabase;
    private final GradeManager gradeManager;

    public ChatListener(UUIDDatabase uuidDatabase, GradeManager gradeManager) {
        this.uuidDatabase = uuidDatabase;
        this.gradeManager = gradeManager;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer) || event.isCommand()) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UUID uuid = player.getUniqueId();
        String gradeName = uuidDatabase.getPlayerGrade(uuid);
        GradeManager.Grade grade = gradeManager.getGrade(gradeName);

        String prefix = (grade != null) ? grade.getFormattedPrefix() : "";
        // Vous pouvez envoyer le message sur le chat si besoin,
        // ici, il est simplement récupéré mais pas envoyé.
        event.setCancelled(true);
        String message = prefix + player.getName() + ": " + event.getMessage();
        // Envoyez ce message où vous le souhaitez (ex: sur un canal de chat).
    }
}
