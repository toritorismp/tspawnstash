package toritori.tspawnstash.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import toritori.tspawnstash.spawnstash.SpawnStash;
import toritori.tspawnstash.spawnstash.SpawnStash5;
import toritori.tspawnstash.util.WandUtil;

import java.util.ArrayList;
import java.util.List;

public class SpawnStashCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String label, String[] args) {

        if (!(sender instanceof Player player))
            return true;

        if (args.length == 0) {

            if (!player.hasPermission("spawnstash.allow")) {
                player.sendMessage("§c権限がありません");
                return true;
            }

            SpawnStash.toggle(player, 1);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (!player.hasPermission("spawnstash.give")) {
                player.sendMessage("§c権限がありません");
                return true;
            }

            player.getInventory().addItem(WandUtil.createWand());
            player.sendMessage("§a棒を受け取りました");
            return true;
        }

        if (!player.hasPermission("spawnstash.allow")) {
            player.sendMessage("§c権限がありません");
            return true;
        }

        switch (args[0]) {

            case "1" -> SpawnStash.toggle(player, 1);

            case "2" -> SpawnStash.toggle(player, 2);

            case "3" -> SpawnStash.toggle(player, 3);

            case "4" -> SpawnStash.toggle(player, 4);

            case "5" -> SpawnStash5.toggle(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {

            if (sender.hasPermission("spawnstash.allow")) {
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
            }

            if (sender.hasPermission("spawnstash.give")) {
                list.add("give");
            }
        }

        return list;
    }
}