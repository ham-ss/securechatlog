package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.chat.MuteManager;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class MslUnmuteSubcommand {

    public static void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c使用法: /msl unmute <player>");
            return;
        }

        String targetName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = target.getUniqueId();

        MuteManager muteManager = SecureChatLoggerPlugin.getInstance().getMuteManager();
        boolean success = muteManager.unmute(uuid);

        String key = success ? "unmute.success" : "unmute.not-muted";
        String msg = LangManager.get(sender, key).replace("{player}", targetName);
        sender.sendMessage(msg);
    }
}

