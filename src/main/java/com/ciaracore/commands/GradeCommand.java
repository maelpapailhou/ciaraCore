package com.ciaracore.commands;

import com.ciaracore.databases.players.UUIDDatabase;
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
        if (args.length < 3 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage("Utilisation : /grade set <joueur> <grade>");
            return;
        }

        String playerName = args[1];
        String rankName = args[2];

        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage("Joueur introuvable.");
            return;
        }

        if (!(sender instanceof ProxiedPlayer) || sender.hasPermission("ciaracore.grade.set")) {
            uuidDatabase.setPlayerGrade(playerUUID, rankName);
            sender.sendMessage("Grade défini avec succès pour " + playerName + " !");
        } else {
            sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
        }
    }
}
