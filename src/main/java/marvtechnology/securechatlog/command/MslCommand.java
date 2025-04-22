package marvtechnology.securechatlog.command;

import marvtechnology.securechatlog.command.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MslCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            MslHelpSubcommand.execute(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "mute":
                MslMuteSubcommand.execute(sender, args);
                break;
            case "unmute":
                MslUnmuteSubcommand.execute(sender, args);
                break;
            case "check":
                MslCheckSubcommand.execute(sender, args);
                break;
            case "verify":
                MslVerifySubcommand.execute(sender);
                break;
            case "status":
                MslStatusSubcommand.execute(sender);
                break;
            case "reload":
                MslReloadSubcommand.execute(sender);
                break;
            case "lang":
                MslLangSubcommand.execute(sender, args);
                break;
            case "testlog":
                MslTestLogSubcommand.execute(sender, args);
                break;
            default:
                sender.sendMessage("§c存在しないサブコマンドです。/msl help を参照してください。");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> subcommands = Arrays.asList(
                "help", "mute", "unmute", "check", "verify", "status", "reload", "lang", "testlog"
        );
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return subcommands.stream()
                    .filter(s -> s.startsWith(prefix))
                    .collect(Collectors.toList()); // Java 8互換
        }
        return Collections.emptyList();
    }
}
