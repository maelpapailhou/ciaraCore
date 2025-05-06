package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import com.ciaracore.managers.RankManager.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * Commande /lookup qui affiche l’UUID, le pseudo et le rank d’un joueur.
 */
public class LookupCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final RankManager rankManager;

    public LookupCommand(UUIDDatabase uuidDatabase, RankManager rankManager) {
        super("lookup");
        this.uuidDatabase = uuidDatabase;
        this.rankManager = rankManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (uuidDatabase == null || rankManager == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Erreur : Base de données ou gestionnaire de ranks non initialisé."));
            return;
        }

        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                displayPlayerInfo(sender, player.getUniqueId());
            } else {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Veuillez spécifier un joueur lorsque vous utilisez cette commande depuis la console."));
            }
            return;
        }

        if (args.length == 1) {
            String playerName = args[0];
            UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);

            if (playerUUID == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Joueur introuvable : " + playerName));
                return;
            }

            if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.lookup.others")) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Vous n'avez pas la permission de voir les informations d'autres joueurs."));
                return;
            }

            displayPlayerInfo(sender, playerUUID);
            return;
        }

        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Utilisation : /lookup [joueur]"));
    }

    private void displayPlayerInfo(CommandSender sender, UUID playerUUID) {
        String playerName = uuidDatabase.getPlayerName(playerUUID);
        String playerRankName = uuidDatabase.getPlayerRank(playerUUID); // méthode renommée correctement
        Rank rank = rankManager.getRank(playerRankName);

        String rankPrefix = (rank != null) ? rank.getFormattedPrefix() : ChatColor.RED + "Inconnu";

        String header = ChatColor.GOLD + "                 " + ChatColor.MAGIC + "||" +
                ChatColor.RESET + "" + ChatColor.AQUA + "" + ChatColor.BOLD + "  LOOKUP  " +
                ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC + "||";
        sender.sendMessage(new TextComponent(header));

        TextComponent uuidComponent = new TextComponent(ChatColor.YELLOW + "UUID" + ChatColor.GRAY + ": ");
        TextComponent uuidValue = new TextComponent(ChatColor.AQUA + playerUUID.toString());
        uuidValue.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour copier").color(ChatColor.GREEN).create()));
        uuidValue.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerUUID.toString()));
        uuidComponent.addExtra(uuidValue);
        sender.sendMessage(uuidComponent);

        TextComponent usernameComponent = new TextComponent(ChatColor.YELLOW + "Pseudo" + ChatColor.GRAY + ": " + ChatColor.AQUA + playerName);
        sender.sendMessage(usernameComponent);

        TextComponent rankComponent = new TextComponent(ChatColor.YELLOW + "Rank" + ChatColor.GRAY + ": ");
        rankComponent.addExtra(new TextComponent(rankPrefix));
        sender.sendMessage(rankComponent);

        sender.sendMessage(new TextComponent(""));
    }
}
