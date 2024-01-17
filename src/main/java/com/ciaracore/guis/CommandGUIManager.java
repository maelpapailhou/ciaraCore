package com.ciaracore.guis;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandGUIManager {

    private final Map<String, BaseGUI> guis;

    public CommandGUIManager(InputStream configFile) {
        this.guis = loadGUIs(configFile);
    }

    private Map<String, BaseGUI> loadGUIs(InputStream configFile) {
        Map<String, BaseGUI> guis = new HashMap<>();
        Yaml yaml = new Yaml();

        try {
            Map<String, Object> yamlData = yaml.load(configFile);

            if (yamlData instanceof Map) {
                for (Map.Entry<String, Object> entry : yamlData.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        Map<String, Object> guiData = (Map<String, Object>) entry.getValue();

                        // Extraire les données de guiData
                        String title = (String) guiData.get("title");
                        int size = (int) guiData.get("size");
                        String viewPermission = (String) guiData.get("viewPermission");
                        String useCommandPermission = (String) guiData.get("useCommandPermission");
                        boolean backEnabled = (boolean) guiData.get("backEnabled");
                        String backItemMaterial = (String) guiData.get("backItemMaterial");
                        String backItemTitle = (String) guiData.get("backItemTitle");
                        List<String> backItemLore = (List<String>) guiData.get("backItemLore");
                        String trigger = (String) guiData.get("trigger");

                        // Passez les données extraites au constructeur de BaseGUI
                        BaseGUI gui = new BaseGUI(title, size, viewPermission, useCommandPermission, backEnabled, backItemMaterial, backItemTitle, backItemLore, trigger);
                        guis.put(entry.getKey(), gui);
                    } else {
                        System.err.println("Erreur lors du chargement des GUIs. Format de données invalide pour la GUI : " + entry.getKey());
                    }
                }
            } else {
                System.err.println("Erreur lors du chargement des GUIs. Le fichier YAML ne contient pas une Map valide.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return guis;
    }

    public void openGUI(ProxiedPlayer player, String guiName) {
        BaseGUI gui = guis.get(guiName);
        if (gui != null) {
            gui.open(player);
        }
    }

    public void handleClick(ProxiedPlayer player, String guiName, int slot) {
        BaseGUI gui = guis.get(guiName);
        if (gui != null) {
            gui.handleClick(player, slot);
        }
    }
}
