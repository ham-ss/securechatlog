package marvtechnology.securechatlog;

import marvtechnology.securechatlog.chat.ChatFilterManager;
import marvtechnology.securechatlog.chat.ChatLogManager;
import marvtechnology.securechatlog.chat.MuteManager;
import marvtechnology.securechatlog.command.MslCommand;
import marvtechnology.securechatlog.config.FilterWordsConfig;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SecureChatLoggerPlugin extends JavaPlugin {

    private static SecureChatLoggerPlugin instance;

    private MuteManager muteManager;
    private ChatLogManager chatLogManager;
    private ChatFilterManager chatFilterManager;

    public static SecureChatLoggerPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig(); // config.yml を初期生成

        // 各種設定ファイルの読み込み
        LangManager.init(this);
        FilterWordsConfig.load(this);

        // 各種マネージャー初期化
        muteManager = new MuteManager(this);
        chatLogManager = new ChatLogManager(this);
        chatLogManager.checkAndRecover(); // ← ログ破損検出と復旧処理
        chatFilterManager = new ChatFilterManager(this);

        // イベント・コマンド登録
        Bukkit.getPluginManager().registerEvents(chatFilterManager, this);
        Bukkit.getPluginCommand("msl").setExecutor(new MslCommand());
        Bukkit.getPluginCommand("msl").setTabCompleter(new MslCommand());

        getLogger().info("SecureChatLog が有効化されました。");
    }

    @Override
    public void onDisable() {
        if (muteManager != null) {
            muteManager.saveAll();
        }
        getLogger().info("SecureChatLog が無効化されました。");
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public ChatLogManager getChatLogManager() {
        return chatLogManager;
    }

    public ChatFilterManager getChatFilterManager() {
        return chatFilterManager;
    }
}

