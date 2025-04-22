package marvtechnology.securechatlog.lang;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class LangManager {

    private static final Map<String, YamlConfiguration> langConfigs = new HashMap<>();
    private static final Map<UUID, String> playerLangMap = new HashMap<>();
    private static final String DEFAULT_LANG = "en_us";

    public static void init(Plugin plugin) {
        langConfigs.clear();
        File langFolder = new File(plugin.getDataFolder(), "lang");

        // mkdirs の結果チェック＋警告解消
        if (!langFolder.exists() && !langFolder.mkdirs()) {
            plugin.getLogger().warning("言語フォルダの作成に失敗しました: " + langFolder.getAbsolutePath());
        }

        File[] files = langFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().endsWith(".yml")) {
                String langKey = file.getName().replace(".yml", "").toLowerCase(Locale.ROOT);
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                langConfigs.put(langKey, config);
            }
        }
    }

    public static String get(CommandSender sender, String key) {
        String lang = DEFAULT_LANG;
        if (sender instanceof Player) {
            UUID uuid = ((Player) sender).getUniqueId();
            lang = playerLangMap.getOrDefault(uuid, DEFAULT_LANG);
        }

        YamlConfiguration config = langConfigs.getOrDefault(lang, langConfigs.get(DEFAULT_LANG));
        return config.contains(key) ? config.getString(key) : "§c<" + key + ">";
    }

    public static boolean setLanguage(Player player, String langCode) {
        if (!langConfigs.containsKey(langCode)) return false;
        playerLangMap.put(player.getUniqueId(), langCode);
        return true;
    }
}
