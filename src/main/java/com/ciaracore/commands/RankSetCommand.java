package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class RankSetCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final RankManager rankManager;

    public RankSetCommand(UUIDDatabase uuidDatabase, RankManager rankManager) {
        super("setrank", "ciaracore.setrank");
        this.uuidDatabase = uuidDatabase;
        this.rankManager = rankManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Utilisation : /setrank <joueur> <grade>");
            return;
        }

        String playerName = args[0];
        String rankName = args[1];

        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage("Joueur introuvable.");
            return;
        }

        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.setrank")) {
            sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return;
        }

        uuidDatabase.setPlayerRank(playerUUID, rankName);
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage("Grade défini avec succès !");
        } else {
            sender.sendMessage("Grade défini avec succès par la console !");
        }
    }
}
