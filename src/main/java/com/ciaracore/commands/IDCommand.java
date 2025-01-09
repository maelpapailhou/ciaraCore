package com.ciaracore.commands;

import com.ciaracore.databases.players.UUIDDatabase;
import com.ciaracore.managers.GradeManager;
import com.ciaracore.managers.GradeManager.Grade;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * Commande /id qui affiche l’UUID et le grade d’un joueur.
 */
public class IDCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final GradeManager gradeManager;

    public IDCommand(UUIDDatabase uuidDatabase, GradeManager gradeManager) {
        super("id");
        this.uuidDatabase = uuidDatabase;
        this.gradeManager = gradeManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (uuidDatabase == null || gradeManager == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Erreur : La base de données ou le gestionnaire de grades n'est pas initialisé."));
            return;
        }

        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                displayPlayerInfo(sender, player.getUniqueId());
            } else {
                sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Veuillez spécifier un nom de joueur."));
            }
        } else if (args.length == 1) {
            String playerName = args[0];
            UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
            if (playerUUID != null) {
                displayPlayerInfo(sender, playerUUID);
            } else {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Joueur non trouvé : " + playerName));
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Utilisation : /id [joueur]"));
        }
    }

    /**
     * Affiche les informations (UUID, nom, grade) d’un joueur au CommandSender.
     */
    private void displayPlayerInfo(CommandSender sender, UUID playerUUID) {
        String playerName = uuidDatabase.getPlayerName(playerUUID);
        String playerGradeName = uuidDatabase.getPlayerGrade(playerUUID);
        Grade grade = gradeManager.getGrade(playerGradeName);

        String gradePrefix = (grade != null) ? grade.getFormattedPrefix() : "Inconnu";

        TextComponent header = new TextComponent(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                    " +
                ChatColor.RESET + "" + ChatColor.GOLD + " ID " +
                ChatColor.STRIKETHROUGH + "                    ");
        sender.sendMessage(header);

        TextComponent uuidComponent = new TextComponent("UUID: " + playerUUID.toString());
        uuidComponent.setColor(ChatColor.GRAY);
        uuidComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Cliquez pour copier").color(ChatColor.YELLOW).create()));
        uuidComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerUUID.toString()));
        sender.sendMessage(uuidComponent);

        TextComponent usernameComponent = new TextComponent(ChatColor.GRAY + "Username: " + ChatColor.WHITE + playerName);
        sender.sendMessage(usernameComponent);

        TextComponent gradeComponent = new TextComponent(ChatColor.GRAY + "Grade: " + gradePrefix + ChatColor.GRAY + " (" + playerGradeName + ")");
        sender.sendMessage(gradeComponent);

        TextComponent footer = new TextComponent(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                    " + "    " + "                    ");
        sender.sendMessage(footer);
    }
}
