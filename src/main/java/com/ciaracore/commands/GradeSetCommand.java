package com.ciaracore.commands;

import com.ciaracore.databases.players.UUIDDatabase;
import com.ciaracore.managers.GradeManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class GradeSetCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final GradeManager gradeManager;

    public GradeSetCommand(UUIDDatabase uuidDatabase, GradeManager gradeManager) {
        super("setgrade", "ciaracore.setgrade");
        this.uuidDatabase = uuidDatabase;
        this.gradeManager = gradeManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Utilisation : /setgrade <joueur> <grade>");
            return;
        }

        String playerName = args[0];
        String gradeName = args[1];

        // Vérification de l'existence du joueur
        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage("Joueur introuvable.");
            return;
        }

        // Vérification des permissions
        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.setgrade")) {
            sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return;
        }

        // Mise à jour du grade
        uuidDatabase.setPlayerGrade(playerUUID, gradeName);
        sender.sendMessage("Grade défini avec succès pour " + playerName + " avec le grade " + gradeName + " !");
    }
}
