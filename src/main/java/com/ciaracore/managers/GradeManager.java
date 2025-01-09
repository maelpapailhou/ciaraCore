package com.ciaracore.managers;

import com.ciaracore.databases.GradeDatabase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeManager {
    private final Map<String, Grade> gradeMap = new HashMap<>();
    private final GradeDatabase gradeDatabase;

    public GradeManager(GradeDatabase gradeDatabase) {
        this.gradeDatabase = gradeDatabase;
        loadGrades();
    }

    private void loadGrades() {
        try {
            List<Grade> grades = gradeDatabase.fetchAllGrades();
            for (Grade grade : grades) {
                gradeMap.put(grade.getName(), grade);
            }
            System.out.println("[GradeManager] " + grades.size() + " grades chargés.");
        } catch (SQLException e) {
            System.err.println("[GradeManager] Erreur lors du chargement des grades : " + e.getMessage());
        }
    }

    public Grade getGrade(String gradeName) {
        return gradeMap.get(gradeName);
    }

    public static class Grade {
        private final String name;
        private final String prefix;

        public Grade(String name, String prefix) {
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
         * Exemple : si le préfixe est "&c[Admin]", cette méthode peut l’interpréter et renvoyer
         * un préfixe formaté avec des couleurs BungeeCord.
         */
        public String getFormattedPrefix() {
            // Si vous utilisez un utilitaire de traduction de couleurs, vous pouvez l'appliquer ici.
            // Par exemple, ChatColor.translateAlternateColorCodes('&', prefix);
            // Pour ce code d'exemple, on retourne le prefix tel quel.
            return prefix;
        }
    }
}
