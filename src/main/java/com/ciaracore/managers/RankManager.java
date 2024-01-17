package com.ciaracore.managers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

public class RankManager {

    private final Map<String, Rank> rankMap;

    public RankManager(InputStream configFile) {
        this.rankMap = loadRanks(configFile);
    }

    public Rank getRank(String rankName) {
        return rankMap.get(rankName);
    }

    private Map<String, Rank> loadRanks(InputStream configFile) {
        Map<String, Rank> ranks = new HashMap<>();
        Yaml yaml = new Yaml();

        try {
            Map<String, Object> yamlData = yaml.load(configFile);

            if (yamlData instanceof Map) {
                for (Map.Entry<String, Object> entry : yamlData.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        Map<String, Object> rankData = (Map<String, Object>) entry.getValue();
                        String name = getString(rankData, "name");
                        String prefix = getString(rankData, "prefix");
                        List<String> permissions = getList(rankData, "permissions");

                        Rank rank = new Rank(name, prefix, permissions.toArray(new String[0]));
                        ranks.put(entry.getKey(), rank);
                    } else {
                        System.err.println("Error loading ranks. Invalid data format for rank: " + entry.getKey());
                    }
                }
            } else {
                System.err.println("Error loading ranks. The YAML file does not contain a valid Map.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ranks;
    }

    private String getString(Map<String, Object> map, String key) {
        return map.containsKey(key) ? ChatColor.translateAlternateColorCodes('&', Objects.toString(map.get(key), "")) : "";
    }

    private List<String> getList(Map<String, Object> map, String key) {
        return map.containsKey(key) && map.get(key) instanceof List ? (List<String>) map.get(key) : new ArrayList<>();
    }

    public static class Rank {

        private final String name;
        private final String prefix;
        private final String[] permissions;

        public Rank(String name, String prefix, String[] permissions) {
            this.name = name;
            this.prefix = prefix;
            this.permissions = permissions;
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        public String[] getPermissions() {
            return permissions;
        }

        public String getFormattedPrefix() {
            return ChatColor.translateAlternateColorCodes('&', prefix);
        }
    }

    public void sendChatMessage(ProxiedPlayer player, String message) {
        Rank rank = getRank(player.getDisplayName()); // Assuming the display name is used as the rank name
        String formattedMessage = rank.getFormattedPrefix() + " " + player.getName() + ChatColor.RESET + ": " + ChatColor.WHITE + message;
        TextComponent chatMessage = new TextComponent(formattedMessage);
        player.sendMessage(chatMessage);
    }
}
