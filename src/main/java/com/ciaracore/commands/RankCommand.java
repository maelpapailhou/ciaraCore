package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.RankManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class RankCommand extends Command {

    private final UUIDDatabase uuidDatabase;
    private final RankManager rankManager;

    public RankCommand(UUIDDatabase uuidDatabase, RankManager rankManager) {
        super("rank");
        this.uuidDatabase = uuidDatabase;
        this.rankManager = rankManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("This command can only be executed by a player.");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();

        if (uuidDatabase == null || rankManager == null) {
            sender.sendMessage("Error: Database or rank manager not initialized.");
            return;
        }

        String playerRankName = uuidDatabase.getPlayerRank(playerUUID);
        RankManager.Rank playerRank = rankManager.getRank(playerRankName);

        if (playerRank != null) {
            player.sendMessage("Your rank: " + playerRank.getName());
            player.sendMessage("Prefix: " + playerRank.getPrefix());
            player.sendMessage("Permissions: " + String.join(", ", playerRank.getPermissions()));
        } else {
            player.sendMessage("Unable to retrieve your rank.");
        }
    }
}
