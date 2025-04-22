package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.lang.LangManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MslLangSubcommand {

    public static void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cこのコマンドはプレイヤーのみ実行できます。");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§c使用法: /msl lang <ja|en|zh|ko>");
            return;
        }

        String langCode = args[1].toLowerCase();
        if (!LangManager.setLanguage((Player) sender, langCode)) {
            sender.sendMessage("§cその言語ファイルは存在しません: " + langCode);
        } else {
            sender.sendMessage("§a言語を " + langCode + " に変更しました。");
        }
    }
}
