package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class RankCommand extends Command {

    private final UUIDDatabase uuidDatabase;

    public RankCommand(UUIDDatabase uuidDatabase) {
        super("rank", "ciaracore.rank"); // Commande /rank, permission ciaracore.rank
        this.uuidDatabase = uuidDatabase;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String language = "fr"; // Langue par défaut

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String playerLang = uuidDatabase.getPlayerOption(player.getUniqueId(), "LANGUAGE");
            if (playerLang != null) {
                language = playerLang;
            }
        }

        if (args.length < 3) {
            sender.sendMessage(getMessage(language, "usage"));
            return;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("set")) {
            handleSetRank(sender, args, language);
        } else {
            sender.sendMessage(getMessage(language, "unknown_command"));
        }
    }

    private void handleSetRank(CommandSender sender, String[] args, String language) {
        if (args.length != 3) {
            sender.sendMessage(getMessage(language, "usage"));
            return;
        }

        String playerName = args[1];
        String rankName = args[2];

        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage(getMessage(language, "player_not_found"));
            return;
        }

        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.rank.set")) {
            sender.sendMessage(getMessage(language, "no_permission"));
            return;
        }

        uuidDatabase.setPlayerRank(playerUUID, rankName);
        sender.sendMessage(getMessage(language, "rank_set", playerName, rankName));
    }

    private String getMessage(String language, String key, Object... args) {
        String message;
        switch (language) {
            case "fr":
                message = getFrenchMessages(key);
                break;
            case "es":
                message = getSpanishMessages(key);
                break;
            default:
                message = getEnglishMessages(key);
                break;
        }

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", args[i].toString());
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message).replace("\\n", "\n");
    }

    private String getFrenchMessages(String key) {
        switch (key) {
            case "usage": return "&eUtilisation : /rank set <joueur> <rang>";
            case "unknown_command": return "&cCommande inconnue.";
            case "player_not_found": return "&cJoueur introuvable.";
            case "no_permission": return "&cVous n'avez pas la permission.";
            case "rank_set": return "&aRang défini pour {0} avec le rang {1} !";
            default: return "&cMessage introuvable : " + key;
        }
    }

    private String getSpanishMessages(String key) {
        switch (key) {
            case "usage": return "&eUso: /rank set <jugador> <rango>";
            case "unknown_command": return "&cComando desconocido.";
            case "player_not_found": return "&cJugador no encontrado.";
            case "no_permission": return "&cNo tienes permiso.";
            case "rank_set": return "&aRango establecido para {0} con el rango {1}!";
            default: return "&cMensaje no encontrado: " + key;
        }
    }

    private String getEnglishMessages(String key) {
        switch (key) {
            case "usage": return "&eUsage: /rank set <player> <rank>";
            case "unknown_command": return "&cUnknown command.";
            case "player_not_found": return "&cPlayer not found.";
            case "no_permission": return "&cYou do not have permission.";
            case "rank_set": return "&aRank set for {0} with rank {1}!";
            default: return "&cMessage not found: " + key;
        }
    }
}
