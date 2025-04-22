package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.logging.Level;

public class MslVerifySubcommand {

    public static void execute(CommandSender sender) {
        File file = SecureChatLoggerPlugin.getInstance().getChatLogManager().getCurrentLogFile();
        if (!file.exists()) {
            sender.sendMessage("§c現在のログファイルが存在しません。");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String lastHash = "00000000000000000000000000000000";
            StringBuilder block = new StringBuilder();
            int blockCount = 0;
            boolean success = true;

            while ((line = reader.readLine()) != null) {
                block.append(line).append("\n");
                if (line.startsWith("HASH:")) {
                    String expectedHash = line.substring(5).trim();
                    String actualHash = sha256(lastHash + block.toString().replace(line + "\n", ""));
                    if (!actualHash.equals(expectedHash)) {
                        sender.sendMessage("§cハッシュ不一致: §f" + expectedHash + " ≠ " + actualHash);
                        success = false;
                        break;
                    }
                    lastHash = actualHash;
                    block.setLength(0);
                    blockCount++;
                }
            }

            if (success) {
                sender.sendMessage("§aログチェーンの整合性を確認しました。正常です。");
                sender.sendMessage("§7検証行数: " + blockCount);
            }
        } catch (Exception e) {
            sender.sendMessage("§cログ検証中にエラーが発生しました。");
            SecureChatLoggerPlugin.getInstance().getLogger().log(Level.WARNING, "ログ検証エラー", e);
        }
    }

    private static String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) hex.append('0');
            hex.append(s);
        }
        return hex.toString();
    }
}
