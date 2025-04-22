package marvtechnology.securechatlog.util;

import java.io.*;
import java.nio.file.Files;

public class LogFileUtil {

    public static void backupFile(File original, File backup) {
        try {
            Files.copy(original.toPath(), backup.toPath());
        } catch (IOException e) {
            System.err.println("[SecureChatLog] ログバックアップ失敗: " + e.getMessage());
        }
    }

    public static boolean isBroken(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String lastHash = "00000000000000000000000000000000";
            StringBuilder block = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                block.append(line).append("\n");
                if (line.startsWith("HASH:")) {
                    String expected = line.substring(5).trim();
                    String actual = HashUtil.sha256(lastHash + block.toString().replace(line + "\n", ""));
                    if (!actual.equals(expected)) {
                        return true;
                    }
                    lastHash = actual;
                    block.setLength(0);
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("[SecureChatLog] ログ整合性検査中のエラー: " + e.getMessage());
            return true;
        }
    }
}
