package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.chat.MuteManager;
import marvtechnology.securechatlog.chat.model.MuteEntry;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class MslStatusSubcommand {

    public static void execute(CommandSender sender) {
        File logFile = SecureChatLoggerPlugin.getInstance().getChatLogManager().getCurrentLogFile();
        String lastHash = SecureChatLoggerPlugin.getInstance().getChatLogManager().getLastHash();
        int lines = countLines(logFile);

        sender.sendMessage("§6== SecureChatLog ステータス ==");
        sender.sendMessage("§7- ログファイル: §f" + logFile.getName());
        sender.sendMessage("§7- 最終ハッシュ: §f" + lastHash);
        sender.sendMessage("§7- 行数: §f" + lines);

        MuteManager muteManager = SecureChatLoggerPlugin.getInstance().getMuteManager();
        Collection<MuteEntry> mutes = muteManager.getAllActiveMutes();
        sender.sendMessage("§7- 現在ミュート中: §f" + mutes.size() + "人");

        for (MuteEntry entry : mutes) {
            sender.sendMessage("§8- " + entry.getPlayerName() + ": あと "
                    + entry.getRemainingFormatted() + "（～ " + entry.getFormattedUntil()
                    + "）理由: " + entry.getReason());
        }
    }

    private static int countLines(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (Exception ignored) {}
        return lines;
    }
}
