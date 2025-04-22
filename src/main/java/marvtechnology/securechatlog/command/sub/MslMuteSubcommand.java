package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.chat.MuteManager;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class MslMuteSubcommand {

    public static void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§c使用法: /msl mute <player> <duration> <reason>");
            return;
        }

        String targetName = args[1];
        String durationArg = args[2];
        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = target.getUniqueId();
        String displayName = target.getName();

        if (displayName == null) {
            sender.sendMessage("§c" + targetName + " は過去にログインしていないため、ミュートできません。");
            return;
        }

        // 時間の解釈（例：10m, 1h, 2d）
        long durationMinutes;
        try {
            durationMinutes = parseDurationToMinutes(durationArg);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§c時間形式が無効です。例: 10m, 1h, 2d");
            return;
        }

        Instant until = Instant.now().plus(durationMinutes, ChronoUnit.MINUTES);
        MuteManager muteManager = SecureChatLoggerPlugin.getInstance().getMuteManager();
        muteManager.mute(uuid, displayName, until, reason);

        String msg = LangManager.get(sender, "mute.success")
                .replace("{player}", displayName)
                .replace("{duration}", durationArg);
        sender.sendMessage(msg);
    }

    private static long parseDurationToMinutes(String input) {
        input = input.toLowerCase();
        if (input.endsWith("m")) {
            return Long.parseLong(input.replace("m", ""));
        } else if (input.endsWith("h")) {
            return Long.parseLong(input.replace("h", "")) * 60;
        } else if (input.endsWith("d")) {
            return Long.parseLong(input.replace("d", "")) * 60 * 24;
        } else {
            throw new IllegalArgumentException("invalid");
        }
    }
}
