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
        if (!(event.getSender() instanceof ProxiedPlayer) || event.isCommand()) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UUID uuid = player.getUniqueId();

        // Récupérer le nom du rank depuis la BDD
        String rankName = uuidDatabase.getPlayerRank(uuid); // cette méthode pourrait aussi être renommée en getPlayerRank()
        RankManager.Rank rank = rankManager.getRank(rankName);

        // Formater le préfixe du rank
        String prefix = (rank != null) ? rank.getFormattedPrefix() : "";

        // Construire le message chat avec couleur
        String formattedMessage = ChatColor.translateAlternateColorCodes('&',
                prefix + " " + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());

        event.setCancelled(true);
        ProxyServer.getInstance().getPlayers().forEach(p -> p.sendMessage(formattedMessage));
    }
}
