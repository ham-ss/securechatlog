package marvtechnology.securechatlog.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FilterWordsConfig {

    private static final Set<String> filteredWords = new HashSet<>();
    private static boolean alertToOps = true;
    private static boolean logToConsole = true;

    public static void load(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "filter_words.yml");
        if (!file.exists()) {
            plugin.saveResource("filter_words.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        filteredWords.clear();
        filteredWords.addAll(config.getStringList("filtered-words"));

        alertToOps = config.getBoolean("options.alert-to-ops", true);
        logToConsole = config.getBoolean("options.log-to-console", true);
    }

    public static Set<String> getFilteredWords() {
        return filteredWords;
    }

    public static boolean isAlertToOps() {
        return alertToOps;
    }

    public static boolean isLogToConsole() {
        return logToConsole;
    }
}
