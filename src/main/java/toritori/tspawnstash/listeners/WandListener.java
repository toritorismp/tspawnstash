package toritori.tspawnstash.listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import toritori.tspawnstash.Tspawnstash;
import toritori.tspawnstash.spawnstash.SpawnStash5;

public class WandListener implements Listener {

    private final NamespacedKey key =
            new NamespacedKey(Tspawnstash.getInstance(), "wand");

    private boolean isWand(ItemStack item) {

        if (item == null)
            return false;

        if (!item.hasItemMeta())
            return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(key, PersistentDataType.BOOLEAN);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isWand(item))
            return;

        Block block = e.getClickedBlock();

        if (block == null)
            return;

        e.setCancelled(true);

        Location loc = block.getLocation();

        // 左クリック = Pos1
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

            SpawnStash5.setPos1(player.getUniqueId(), loc);

            player.sendMessage(
                    "§aPos1を設定しました §7("
                            + loc.getBlockX() + ", "
                            + loc.getBlockY() + ", "
                            + loc.getBlockZ() + ")"
            );
        }

        // 右クリック = Pos2
        else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            SpawnStash5.setPos2(player.getUniqueId(), loc);

            player.sendMessage(
                    "§aPos2を設定しました §7("
                            + loc.getBlockX() + ", "
                            + loc.getBlockY() + ", "
                            + loc.getBlockZ() + ")"
            );
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        ItemStack item = e.getPlayer()
                .getInventory()
                .getItemInMainHand();

        if (!isWand(item))
            return;

        e.setCancelled(true);
    }
}