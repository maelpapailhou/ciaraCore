package com.ciaracore.guis;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class BaseGUI {

    private final String title;
    private final int size;
    private final String viewPermission;
    private final String useCommandPermission;
    private final boolean backEnabled;
    private final String backItemMaterial;
    private final String backItemTitle;
    private final List<String> backItemLore;
    private final String trigger;

    // Constructeur pour initialiser les attributs de la classe
    public BaseGUI(String title, int size, String viewPermission, String useCommandPermission,
                   boolean backEnabled, String backItemMaterial, String backItemTitle, List<String> backItemLore,
                   String trigger) {
        this.title = title;
        this.size = size;
        this.viewPermission = viewPermission;
        this.useCommandPermission = useCommandPermission;
        this.backEnabled = backEnabled;
        this.backItemMaterial = backItemMaterial;
        this.backItemTitle = backItemTitle;
        this.backItemLore = backItemLore;
        this.trigger = trigger;
    }

    // Méthode pour obtenir le déclencheur (trigger) de la GUI
    public String getTrigger() {
        return trigger;
    }

    // Méthode appelée pour ouvrir la GUI pour un joueur donné
    public void open(ProxiedPlayer player) {
        // TODO: Implémentez la logique pour ouvrir la GUI ici
        // Utilisez la classe Inventory de BungeeCord pour créer et afficher l'interface utilisateur
        // Assurez-vous de vérifier les autorisations nécessaires avant d'ouvrir la GUI
    }

    // Méthode appelée lorsqu'un joueur clique sur un élément de la GUI
    public void handleClick(ProxiedPlayer player, int slot) {
        // TODO: Implémentez la logique pour gérer le clic sur les items de la GUI ici
        // Utilisez le numéro de slot pour déterminer quel élément a été cliqué
    }
}
