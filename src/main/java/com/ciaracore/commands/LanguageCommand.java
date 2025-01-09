package com.ciaracore.commands;

import com.ciaracore.databases.UUIDDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LanguageCommand extends Command {

    private final UUIDDatabase uuidDatabase;

    // Langues prises en charge
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("fr", "en", "es");

    public LanguageCommand(UUIDDatabase uuidDatabase) {
        super("language");
        this.uuidDatabase = uuidDatabase;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        // Vérifie si un argument a été fourni
        if (args.length != 1) {
            sender.sendMessage("Utilisation : /language <fr|en|es>");
            return;
        }

        String language = args[0].toLowerCase();

        // Vérifie si la langue est valide
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            sender.sendMessage("Langue invalide. Langues disponibles : fr, en, es");
            return;
        }

        UUID playerUUID = player.getUniqueId();

        // Mise à jour de la langue dans la base de données
        try {
            uuidDatabase.setPlayerLanguage(playerUUID, language);
            sender.sendMessage("Votre langue a été définie sur : " + language);
        } catch (Exception e) {
            sender.sendMessage("Une erreur est survenue lors de la mise à jour de votre langue.");
            e.printStackTrace();
        }
    }
}
