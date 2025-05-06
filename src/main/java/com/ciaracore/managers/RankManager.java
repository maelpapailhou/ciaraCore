package com.ciaracore.managers;

import com.ciaracore.databases.RankDatabase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {
    private final Map<String, Rank> rankMap = new HashMap<>();
    private final RankDatabase rankDatabase;

    public RankManager(RankDatabase rankDatabase) {
        this.rankDatabase = rankDatabase;
        loadRanks();
    }

    private void loadRanks() {
        try {
            List<Rank> ranks = rankDatabase.fetchAllRanks();
            for (Rank rank : ranks) {
                rankMap.put(rank.getName(), rank);
            }
            System.out.println("[RankManager] " + ranks.size() + " ranks chargés.");
        } catch (SQLException e) {
            System.err.println("[RankManager] Erreur lors du chargement des ranks : " + e.getMessage());
        }
    }

    public Rank getRank(String rankName) {
        return rankMap.get(rankName);
    }

    public static class Rank {
        private final String name;
        private final String prefix;

        public Rank(String name, String prefix) {
            this.name = name;
            this.prefix = prefix;
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        /**
         * Retourne le préfixe formaté avec les codes couleurs de BungeeCord/Minecraft.
         */
        public String getFormattedPrefix() {
            // À adapter selon votre API de traduction de couleurs (si nécessaire)
            return prefix;
        }
    }
}
