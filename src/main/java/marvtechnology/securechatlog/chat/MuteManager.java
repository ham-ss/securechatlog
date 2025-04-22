package marvtechnology.securechatlog.chat;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.chat.model.MuteEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class MuteManager {

    private final File file;
    private final YamlConfiguration config;
    private final Map<UUID, MuteEntry> mutedPlayers = new HashMap<>();

    public MuteManager(SecureChatLoggerPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "muted_players.yml");
        if (!file.exists()) {
            plugin.saveResource("muted_players.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        load(plugin);
    }

    private void load(SecureChatLoggerPlugin plugin) {
        mutedPlayers.clear();

        if (!config.isConfigurationSection("muted")) return;

        var section = config.getConfigurationSection("muted");
        if (section == null) return;

        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            String path = "muted." + key;
            String uuidStr = config.getString(path + ".uuid");
            String untilStr = config.getString(path + ".muted_until");
            String reason = config.getString(path + ".reason");

            if (uuidStr == null || untilStr == null) continue;

            try {
                UUID uuid = UUID.fromString(uuidStr);
                Instant until = Instant.parse(untilStr);
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                mutedPlayers.put(uuid, new MuteEntry(uuid, player.getName(), until, reason));
            } catch (Exception e) {
                plugin.getLogger().warning("ミュート情報の読み込み中にエラー: " + e.getMessage());
            }
        }
    }

    public void saveAll() {
        config.set("muted", null);
        for (MuteEntry entry : mutedPlayers.values()) {
            String path = "muted." + entry.getPlayerName();
            config.set(path + ".uuid", entry.getUuid().toString());
            config.set(path + ".muted_until", entry.getMutedUntil().toString());
            config.set(path + ".reason", entry.getReason());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            SecureChatLoggerPlugin.getInstance().getLogger().warning("ミュートファイルの保存に失敗しました: " + e.getMessage());
        }
    }

    public boolean isMuted(UUID uuid) {
        MuteEntry entry = mutedPlayers.get(uuid);
        if (entry == null) return false;
        if (entry.isExpired()) {
            mutedPlayers.remove(uuid);
            return false;
        }
        return true;
    }

    public Optional<MuteEntry> getMuteEntry(UUID uuid) {
        if (!isMuted(uuid)) return Optional.empty();
        return Optional.ofNullable(mutedPlayers.get(uuid));
    }

    public void mute(UUID uuid, String name, Instant until, String reason) {
        mutedPlayers.put(uuid, new MuteEntry(uuid, name, until, reason));
        saveAll();
    }

    public boolean unmute(UUID uuid) {
        boolean removed = mutedPlayers.remove(uuid) != null;
        if (removed) saveAll();
        return removed;
    }

    public Collection<MuteEntry> getAllActiveMutes() {
        List<MuteEntry> list = new ArrayList<>();
        for (MuteEntry entry : mutedPlayers.values()) {
            if (!entry.isExpired()) list.add(entry);
        }
        return list;
    }
}
