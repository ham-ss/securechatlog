package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import marvtechnology.securechatlog.config.FilterWordsConfig;
import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.command.CommandSender;

public class MslReloadSubcommand {

    public static void execute(CommandSender sender) {
        SecureChatLoggerPlugin plugin = SecureChatLoggerPlugin.getInstance();

        // 再読み込み
        plugin.reloadConfig();
        LangManager.init(plugin);
        FilterWordsConfig.load(plugin);

        sender.sendMessage("§a設定ファイルを再読み込みしました。");
    }
}
