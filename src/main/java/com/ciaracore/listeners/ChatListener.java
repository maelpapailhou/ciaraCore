package com.ciaracore.listeners;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
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
        if (event.isCommand()) {
            return; // Ignorer les messages de commande
        }

        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            UUID playerUUID = player.getUniqueId();

            String playerName = uuidDatabase.getPlayerName(playerUUID);
            String playerRankName = uuidDatabase.getPlayerRank(playerUUID);

            if (playerName != null && playerRankName != null) {
                RankManager.Rank playerRank = rankManager.getRank(playerRankName);

                if (playerRank != null) {
                    TextComponent prefixComponent = new TextComponent(playerRank.getFormattedPrefix() + " " + playerName);
                    TextComponent colonComponent = new TextComponent(": ");
                    colonComponent.setColor(ChatColor.GRAY); // Définir la couleur du ":" en gris foncé

                    String message = event.getMessage();
                    TextComponent messageComponent = new TextComponent(message);
                    messageComponent.setColor(ChatColor.WHITE); // Définir la couleur du texte en blanc

                    // Construire le message complet avec le format personnalisé
                    BaseComponent[] components = new BaseComponent[]{prefixComponent, colonComponent, messageComponent};

                    // Envoyer le message à tous les joueurs connectés sur le même serveur
                    for (ProxiedPlayer recipient : player.getServer().getInfo().getPlayers()) {
                        recipient.sendMessage(components);
                    }

                    // Annuler le message de chat original
                    event.setCancelled(true);
                }
            }
        }
    }
}
