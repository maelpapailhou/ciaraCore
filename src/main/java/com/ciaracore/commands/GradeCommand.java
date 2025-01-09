package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.GradeManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class GradeCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final GradeManager gradeManager;

    public GradeCommand(UUIDDatabase uuidDatabase, GradeManager gradeManager) {
        super("grade", "ciaracore.grade");
        this.uuidDatabase = uuidDatabase;
        this.gradeManager = gradeManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Utilisation : /grade set <joueur> <grade>");
            return;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set":
                handleSetGrade(sender, args);
                break;

            default:
                sender.sendMessage("Commande inconnue. Utilisation : /grade set <joueur> <grade>");
                break;
        }
    }

    private void handleSetGrade(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Utilisation : /grade set <joueur> <grade>");
            return;
        }

        String playerName = args[1];
        String gradeName = args[2];

        // Vérification de l'existence du joueur
        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage("Joueur introuvable.");
            return;
        }

        // Vérification des permissions
        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.grade.set")) {
            sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return;
        }

        // Mise à jour du grade
        uuidDatabase.setPlayerGrade(playerUUID, gradeName);
        sender.sendMessage("Grade défini avec succès pour " + playerName + " avec le grade " + gradeName + " !");
    }
}
