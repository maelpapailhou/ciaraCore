package com.ciaracore.managers;

import net.md_5.bungee.api.ChatColor;

import java.io.InputStream;
import java.util.Properties;

public class LanguageManager {
    private final Properties messages = new Properties();

    public LanguageManager(String language) {
        loadLanguage(language);
    }

    private void loadLanguage(String language) {
        String fileName = "language_" + language + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                messages.load(input);
            } else {
                // Si le fichier de langue n'existe pas, chargez la langue par défaut (en)
                loadLanguage("en");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier de langue : " + fileName);
            e.printStackTrace();
        }
    }

    public String getMessage(String key, Object... args) {
        String message = messages.getProperty(key, "Message introuvable : " + key);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", args[i].toString());
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace("\\n", "\n");
    }
}
