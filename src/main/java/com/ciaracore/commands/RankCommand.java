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
        super("grade", "ciaracore.grade"); // Command name "/grade", permission "ciaracore.grade"
        this.uuidDatabase = uuidDatabase;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Default language is French
        String language = "fr";

        // Check if sender is a player and get the language preference
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            language = uuidDatabase.getPlayerOption(player.getUniqueId(), "LANGUAGE");
            if (language == null) {
                language = "fr"; // Fallback language if none is set
            }
        }

        // If arguments are insufficient, send the usage message
        if (args.length < 3) {
            sender.sendMessage(getMessage(language, "usage"));
            return;
        }

        // Handle sub-commands
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set":
                handleSetGrade(sender, args, language);
                break;
            default:
                sender.sendMessage(getMessage(language, "unknown_command"));
                break;
        }
    }

    private void handleSetGrade(CommandSender sender, String[] args, String language) {
        if (args.length != 3) {
            sender.sendMessage(getMessage(language, "usage"));
            return;
        }

        String playerName = args[1];
        String gradeName = args[2];

        // Get player's UUID from the database
        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage(getMessage(language, "player_not_found"));
            return;
        }

        // Ensure sender has permission to set grades
        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.grade.set")) {
            sender.sendMessage(getMessage(language, "no_permission"));
            return;
        }

        // Update the player's grade in the database
        uuidDatabase.setPlayerGrade(playerUUID, gradeName);

        // Send confirmation message
        sender.sendMessage(getMessage(language, "grade_set", playerName, gradeName));
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

        // Replace placeholders in the message
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", args[i].toString());
            }
        }

        // Translate BungeeCord color codes and return
        return ChatColor.translateAlternateColorCodes('&', message).replace("\\n", "\n");
    }

    private String getFrenchMessages(String key) {
        switch (key) {
            case "usage":
                return "&eUtilisation : /grade set <joueur> <grade>";
            case "unknown_command":
                return "&cCommande inconnue.";
            case "player_not_found":
                return "&cJoueur introuvable.";
            case "no_permission":
                return "&cVous n'avez pas la permission.";
            case "grade_set":
                return "&aGrade défini pour {0} avec le grade {1} !";
            default:
                return "&cMessage introuvable : " + key;
        }
    }

    private String getSpanishMessages(String key) {
        switch (key) {
            case "usage":
                return "&eUso: /grade set <jugador> <rango>";
            case "unknown_command":
                return "&cComando desconocido.";
            case "player_not_found":
                return "&cJugador no encontrado.";
            case "no_permission":
                return "&cNo tienes permiso.";
            case "grade_set":
                return "&aRango establecido para {0} con el rango {1}!";
            default:
                return "&cMensaje no encontrado: " + key;
        }
    }

    private String getEnglishMessages(String key) {
        switch (key) {
            case "usage":
                return "&eUsage: /grade set <player> <grade>";
            case "unknown_command":
                return "&cUnknown command.";
            case "player_not_found":
                return "&cPlayer not found.";
            case "no_permission":
                return "&cYou do not have permission.";
            case "grade_set":
                return "&aGrade set for {0} with grade {1}!";
            default:
                return "&cMessage not found: " + key;
        }
    }
}
