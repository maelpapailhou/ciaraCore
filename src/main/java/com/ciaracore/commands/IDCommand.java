package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.packet.Chat;

import java.util.UUID;

public class IDCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final RankManager rankManager;

    public IDCommand(UUIDDatabase uuidDatabase, RankManager rankManager) {
        super("id");
        this.uuidDatabase = uuidDatabase;
        this.rankManager = rankManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (uuidDatabase == null || rankManager == null) {
            sender.sendMessage(ChatColor.RED + "Erreur : La base de données ou le gestionnaire de rangs n'est pas initialisé.");
            return;
        }

        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                displayPlayerInfo(player, player.getUniqueId());
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Veuillez spécifier un nom de joueur.");
            }
        } else if (args.length == 1) {
            String playerName = args[0];
            UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
            if (playerUUID != null) {
                displayPlayerInfo(sender, playerUUID);
            } else {
                sender.sendMessage(ChatColor.RED + "Joueur non trouvé : " + playerName);
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Utilisation : /id [joueur]");
        }
    }

    private void displayPlayerInfo(CommandSender sender, UUID playerUUID) {
        String playerName = uuidDatabase.getPlayerName(playerUUID);
        String playerRankName = uuidDatabase.getPlayerRank(playerUUID); // Nom exact du rang dans la base de données
        RankManager.Rank playerRank = rankManager.getRank(playerRankName);

        // Convertir le préfixe du format YAML en format ChatColor
        String rankPrefix = playerRank != null ? ChatColor.translateAlternateColorCodes('&', playerRank.getPrefix()) : "Inconnu";

        int playerCoins = uuidDatabase.getPlayerCoins(playerUUID); // Assurez-vous que cette méthode existe

        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                    " +
                ChatColor.RESET + "" + ChatColor.GOLD + " ID " +
                ChatColor.STRIKETHROUGH + "                    ");
        sender.sendMessage(new ComponentBuilder("UUID: ")
                .color(ChatColor.GRAY)
                .append(playerUUID.toString())
                .color(ChatColor.WHITE)
                .italic(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour copier").color(ChatColor.YELLOW).create()))
                .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerUUID.toString()))
                .create());
        sender.sendMessage(ChatColor.GRAY + "Username: " + ChatColor.WHITE + playerName);

        // Afficher le rang avec le préfixe et le nom exact du rang
        sender.sendMessage(new ComponentBuilder(ChatColor.GRAY + "Rank: ")
                .append(rankPrefix + ChatColor.GRAY + " (" + playerRankName + ")")
                .create());

        sender.sendMessage(ChatColor.GRAY + "Coins: " + ChatColor.GREEN + playerCoins);
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                    " + "    " + "                    ");
    }

}
