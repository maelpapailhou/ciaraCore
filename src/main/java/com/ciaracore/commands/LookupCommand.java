package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import com.ciaracore.managers.RankManager.Grade;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * Commande /id qui affiche l’UUID et le grade d’un joueur.
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
            sender.sendMessage(new TextComponent(ChatColor.RED + "Erreur : La base de données ou le gestionnaire de grades n'est pas initialisé."));
            return;
        }

        // Cas 1 : Le joueur fait /lookup pour lui-même
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                displayPlayerInfo(sender, player.getUniqueId());
            } else {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Veuillez spécifier un joueur lorsque vous utilisez cette commande depuis la console."));
            }
            return;
        }

        // Cas 2 : Le joueur fait /lookup <pseudo>
        if (args.length == 1) {
            String playerName = args[0];
            UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);

            if (playerUUID == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Joueur introuvable : " + playerName));
                return;
            }

            // Vérification des permissions pour voir les informations d'autres joueurs
            if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.lookup.others")) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Vous n'avez pas la permission de voir les informations d'autres joueurs."));
                return;
            }

            // Affichage des informations du joueur cible
            displayPlayerInfo(sender, playerUUID);
            return;
        }

        // Cas 3 : Mauvais usage de la commande
        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Utilisation : /lookup [joueur]"));
    }

    /**
     * Affiche les informations (UUID, nom, grade) d’un joueur au CommandSender.
     */
    private void displayPlayerInfo(CommandSender sender, UUID playerUUID) {
        String playerName = uuidDatabase.getPlayerName(playerUUID);
        String playerGradeName = uuidDatabase.getPlayerGrade(playerUUID);
        Grade grade = rankManager.getGrade(playerGradeName);

        String gradePrefix = (grade != null) ? grade.getFormattedPrefix() : ChatColor.RED + "Inconnu";

        // En-tête avec style
        String header = ChatColor.GOLD + "                 " + ChatColor.MAGIC + "||" +
                ChatColor.RESET + "" + ChatColor.AQUA + "" + ChatColor.BOLD + "  LOOKUP  "
                + "" + ChatColor.RESET + ""
                + ChatColor.GOLD + "" + ChatColor.MAGIC + "||";
        ;
        sender.sendMessage(new TextComponent(header));

        // UUID avec interaction
        TextComponent uuidComponent = new TextComponent(ChatColor.YELLOW + "UUID" + ChatColor.GRAY + ": ");
        TextComponent uuidValue = new TextComponent(ChatColor.AQUA + playerUUID.toString());
        uuidValue.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour copier").color(ChatColor.GREEN).create()));
        uuidValue.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerUUID.toString()));
        uuidComponent.addExtra(uuidValue);
        sender.sendMessage(uuidComponent);

        // Pseudo
        TextComponent usernameComponent = new TextComponent(ChatColor.YELLOW + "Pseudo" + ChatColor.GRAY + ": " + ChatColor.AQUA + playerName);
        sender.sendMessage(usernameComponent);

        // Grade
        TextComponent gradeComponent = new TextComponent(ChatColor.YELLOW + "Grade" + ChatColor.GRAY + ": ");
        gradeComponent.addExtra(new TextComponent(gradePrefix));
        sender.sendMessage(gradeComponent);

        sender.sendMessage(new TextComponent(""));

    }

}
