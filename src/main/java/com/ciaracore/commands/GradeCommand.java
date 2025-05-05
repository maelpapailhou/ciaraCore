package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import com.ciaracore.managers.GradeManager;
import net.md_5.bungee.api.ChatColor;
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
        String language = "fr"; // Langue par défaut
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            language = uuidDatabase.getPlayerOption(player.getUniqueId(), "LANGUAGE"); // Assurez-vous que cette méthode existe
            if (language == null) {
                language = "fr"; // Fallback au cas où la langue n'est pas définie
            }
        }

        if (args.length < 1) {
            sender.sendMessage(getMessage(language, "usage"));
            return;
        }

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

        UUID playerUUID = uuidDatabase.getPlayerUUID(playerName);
        if (playerUUID == null) {
            sender.sendMessage(getMessage(language, "player_not_found"));
            return;
        }

        if (sender instanceof ProxiedPlayer && !sender.hasPermission("ciaracore.grade.set")) {
            sender.sendMessage(getMessage(language, "no_permission"));
            return;
        }

        uuidDatabase.setPlayerGrade(playerUUID, gradeName);
        sender.sendMessage(getMessage(language, "grade_set", playerName, gradeName));
    }

    private String getMessage(String language, String key, Object... args) {
        // Simule le chargement des messages pour la langue donnée
        // Remplacez cela par un gestionnaire de langues réel (ex. LanguageManager)
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

        // Remplace les variables {0}, {1}, etc. par les valeurs fournies
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", args[i].toString());
            }
        }

        // Gère les codes de couleur BungeeCord
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
