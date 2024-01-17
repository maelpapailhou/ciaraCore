package com.ciaracore.guis;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager {

    private final Map<String, BaseGUI> guis;

    public GUIManager(InputStream configFile) {
        this.guis = loadGUIs(configFile);
    }

    public Map<String, BaseGUI> loadGUIs(InputStream configFile) {
        Map<String, BaseGUI> guis = new HashMap<>();
        Yaml yaml = new Yaml();

        try {
            Map<String, Map<String, Object>> yamlData = yaml.load(configFile);

            if (yamlData != null) {
                for (Map.Entry<String, Map<String, Object>> entry : yamlData.entrySet()) {
                    Map<String, Object> guiData = entry.getValue();

                    String title = (String) guiData.get("title");
                    int size = (int) guiData.get("size");
                    String viewPermission = (String) guiData.get("view-permission");
                    String useCommandPermission = (String) guiData.get("use-command-permission");
                    boolean backEnabled = (boolean) guiData.get("back-enabled");
                    String backItemMaterial = (String) guiData.get("back-item.material");
                    String backItemTitle = (String) guiData.get("back-item.title");
                    List<String> backItemLore = (List<String>) guiData.get("back-item.lore");
                    String trigger = (String) guiData.get("trigger");

                    BaseGUI gui = new BaseGUI(title, size, viewPermission, useCommandPermission, backEnabled,
                            backItemMaterial, backItemTitle, backItemLore, trigger);

                    guis.put(entry.getKey(), gui);
                }
            } else {
                System.err.println("Erreur lors du chargement des GUIs. Le fichier YAML est vide ou mal formaté.");
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
