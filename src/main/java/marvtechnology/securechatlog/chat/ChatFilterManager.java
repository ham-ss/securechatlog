package marvtechnology.securechatlog.chat;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.config.FilterWordsConfig;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatFilterManager implements Listener {

    private final SecureChatLoggerPlugin plugin;

    public ChatFilterManager(SecureChatLoggerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        Set<String> blocked = FilterWordsConfig.getFilteredWords();

        for (String word : blocked) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                event.setCancelled(true);

                if (FilterWordsConfig.isAlertToOps()) {
                    String alert = LangManager.get(player, "filter.detected")
                            .replace("{player}", player.getName())
                            .replace("{word}", word)
                            .replace("{message}", message);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.isOp()) {
                            online.sendMessage(ChatColor.RED + alert);
                        }
                    }
                }

                if (FilterWordsConfig.isLogToConsole()) {
                    plugin.getLogger().warning("[FilteredChat] <" + player.getName() + "> " + message + " (NG: " + word + ")");
                }
                return;
            }
        }

        String ip = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "UNKNOWN";
        plugin.getChatLogManager().log(player.getUniqueId(), ip, player.getName(), message);
    }
}
