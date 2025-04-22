package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.chat.MuteManager;
import marvtechnology.securechatlog.chat.model.MuteEntry;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.UUID;

public class MslCheckSubcommand {


    public static void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c使用法: /msl check <player>");
            return;
        }

        String targetName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = target.getUniqueId();

        MuteManager muteManager = SecureChatLoggerPlugin.getInstance().getMuteManager();
        Optional<MuteEntry> entryOpt = muteManager.getMuteEntry(uuid);

        if (entryOpt.isPresent()) {
            MuteEntry entry = entryOpt.get();

            sender.sendMessage("§c" + targetName + " は現在ミュート中です：");
            sender.sendMessage("§7- 残り時間: §f" + entry.getRemainingFormatted());
            sender.sendMessage("§7- 解除予定: §f" + entry.getFormattedUntil());
            sender.sendMessage("§7- 理由: §f" + entry.getReason());
        } else {
            String msg = LangManager.get(sender, "check.not-muted")
                    .replace("{player}", targetName);
            sender.sendMessage(msg);
        }
    }
}
