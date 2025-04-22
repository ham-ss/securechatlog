package marvtechnology.securechatlog.chat;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.util.HashUtil;
import marvtechnology.securechatlog.util.LogFileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ChatLogManager {

    private final SecureChatLoggerPlugin plugin;
    private final File logDir;
    private File currentLogFile;
    private String lastHash = "00000000000000000000000000000000";

    public ChatLogManager(SecureChatLoggerPlugin plugin) {
        this.plugin = plugin;
        this.logDir = new File(plugin.getDataFolder(), "logs");
        if (!logDir.exists() && !logDir.mkdirs()) {
            plugin.getLogger().warning("ログディレクトリの作成に失敗しました: " + logDir.getAbsolutePath());
        }
        loadOrCreateLogFile();
    }

    private void loadOrCreateLogFile() {
        String fileName = "secure_chat_" + DateTimeFormatter.ofPattern("yyyy-MM")
                .format(Instant.now().atZone(ZoneId.systemDefault())) + ".log";
        currentLogFile = new File(logDir, fileName);
        if (!currentLogFile.exists()) {
            try (PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(currentLogFile, true), StandardCharsets.UTF_8))) {
                writer.println("--- Secure Chat Log ---");
                writer.println("# Created: " + Instant.now());
                writer.println("# HashChain-Seed: " + lastHash);
            } catch (IOException e) {
                plugin.getLogger().warning("ログファイルの作成に失敗しました。");
            }
        } else {
            recoverLastHash();
        }
    }

    private void recoverLastHash() {
        try {
            String lastLine = "";
            for (String line : Files.readAllLines(currentLogFile.toPath(), StandardCharsets.UTF_8)) {
                if (line.startsWith("HASH:")) {
                    lastLine = line;
                }
            }
            if (lastLine.startsWith("HASH:")) {
                lastHash = lastLine.substring(5).trim();
            }
        } catch (IOException e) {
            plugin.getLogger().warning("最終ハッシュの読み取りに失敗しました。");
        }
    }

    public void checkAndRecover() {
        if (LogFileUtil.isBroken(currentLogFile)) {
            plugin.getLogger().warning("ログファイルが破損しています。リカバリを開始します。");

            File broken = new File(currentLogFile.getParent(),
                    currentLogFile.getName().replace(".log", "_broken.log"));
            LogFileUtil.backupFile(currentLogFile, broken);

            File recovered = new File(currentLogFile.getParent(),
                    currentLogFile.getName().replace(".log", "_recovered.log"));
            try (PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(recovered, true), StandardCharsets.UTF_8))) {
                writer.println("--- Secure Chat Log (Recovered) ---");
                writer.println("# Created: " + Instant.now());
                writer.println("# HashChain-Seed: 00000000000000000000000000000000");
            } catch (IOException e) {
                plugin.getLogger().severe("復旧ログファイルの作成に失敗しました。");
            }

            currentLogFile = recovered;
            lastHash = "00000000000000000000000000000000";
        }
    }

    public void log(UUID uuid, String ip, String playerName, String message) {
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault()).format(Instant.now());

        StringBuilder builder = new StringBuilder();
        builder.append("[").append(time).append("]\n");
        builder.append("UUID: ").append(uuid).append("\n"); // ← toString() 不要
        builder.append("IP: ").append(ip).append("\n");     // ← toString() 不要
        builder.append("<").append(playerName).append("> ").append(message).append("\n");

        String hashInput = lastHash + builder;
        String newHash = HashUtil.sha256(hashInput);
        builder.append("HASH: ").append(newHash).append("\n");

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(currentLogFile, true), StandardCharsets.UTF_8))) {
            writer.print(builder);
        } catch (IOException e) {
            plugin.getLogger().warning("チャットログの書き込みに失敗しました。");
        }

        lastHash = newHash;
    }

    public String getLastHash() {
        return lastHash;
    }

    public File getCurrentLogFile() {
        return currentLogFile;
    }
}
