package com.ciaracore.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabManager implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        // Récupérez le grade du joueur depuis votre système de gestion des grades
        String playerName = player.getName();
        String playerRank = "Joueur"; // Remplacez cela par la logique pour obtenir le grade du joueur

        // Créez le préfixe avec la couleur du grade
        String rankPrefix = ChatColor.GREEN + "[" + ChatColor.YELLOW + playerRank + ChatColor.GREEN + "]";

        // Créez l'en-tête et le pied du TabList
        String header = ChatColor.translateAlternateColorCodes('&', "&6Bienvenue sur le serveur !");
        String footer = ChatColor.translateAlternateColorCodes('&', "&aMerci de jouer sur notre serveur !");

        // Modifiez le TabList
        player.resetTabHeader(); // Effacez l'en-tête et le pied actuels
        player.setTabHeader(TextComponent.fromLegacyText(header.replace("%prefix%", rankPrefix).replace("%player%", playerName)),
                TextComponent.fromLegacyText(footer.replace("%prefix%", rankPrefix).replace("%player%", playerName)));
    }
}
