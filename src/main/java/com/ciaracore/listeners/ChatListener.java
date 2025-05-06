package com.ciaracore.listeners;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ChatListener implements Listener {

    private final UUIDDatabase uuidDatabase;
    private final RankManager rankManager;

    public ChatListener(UUIDDatabase uuidDatabase, RankManager rankManager) {
        this.uuidDatabase = uuidDatabase;
        this.rankManager = rankManager;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        // Vérifiez que l'expéditeur est un joueur et que ce n'est pas une commande
        if (!(event.getSender() instanceof ProxiedPlayer) || event.isCommand()) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UUID uuid = player.getUniqueId();

        // Récupérez le grade du joueur
        String gradeName = uuidDatabase.getPlayerGrade(uuid);
        RankManager.Grade grade = rankManager.getGrade(gradeName);

        // Ajoutez le préfixe du grade (s'il existe)
        String prefix = (grade != null) ? grade.getFormattedPrefix() : "";

        // Construisez le message formaté
        String formattedMessage = ChatColor.translateAlternateColorCodes('&',
                prefix + " " + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());

        // Annulez l'envoi du message brut
        event.setCancelled(true);

        // Diffusez le message formaté à tous les joueurs connectés
        ProxyServer.getInstance().getPlayers().forEach(p -> p.sendMessage(formattedMessage));
    }
}
