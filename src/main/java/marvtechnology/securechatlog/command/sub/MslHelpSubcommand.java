package marvtechnology.securechatlog.command.sub;

import org.bukkit.command.CommandSender;

public class MslHelpSubcommand {

    public static void execute(CommandSender sender) {
        sender.sendMessage("§6== SecureChatLog コマンド一覧 ==");
        sender.sendMessage("§e/msl mute <player> <time> <reason> §7- プレイヤーをミュートします");
        sender.sendMessage("§e/msl unmute <player> §7- ミュートを解除します");
        sender.sendMessage("§e/msl check <player> §7- ミュート状態を確認します");
        sender.sendMessage("§e/msl verify §7- チャットログの整合性を検証します");
        sender.sendMessage("§e/msl status §7- 現在の状態を表示します");
        sender.sendMessage("§e/msl reload §7- 設定ファイルを再読み込みします");
        sender.sendMessage("§e/msl lang <code> §7- 自分の言語を変更します");
        sender.sendMessage("§e/msl testlog <message> §7- テストログを記録します");
    }
}
