package marvtechnology.securechatlog.command.sub;

import marvtechnology.securechatlog.SecureChatLoggerPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MslTestLogSubcommand {

    public static void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cこのコマンドはプレイヤーのみ実行できます。");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§c使用法: /msl testlog <message>");
            return;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        String ip = player.getAddress() != null
                ? player.getAddress().getAddress().getHostAddress()
                : "UNKNOWN";
        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        SecureChatLoggerPlugin.getInstance().getChatLogManager().log(uuid, ip, player.getName(), message);
        sender.sendMessage("§aテストログを書き込みました。内容: " + message);
    }
}
