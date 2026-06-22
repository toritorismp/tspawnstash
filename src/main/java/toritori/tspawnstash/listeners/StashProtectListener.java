package toritori.tspawnstash.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import toritori.tspawnstash.spawnstash.StashManager;

public class StashProtectListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Block block = e.getBlock();

        if (!StashManager.isStashBlock(block.getLocation()))
            return;

        e.setCancelled(true);

        e.getPlayer().sendActionBar(
                net.kyori.adventure.text.Component.text(
                        "§cこのSpawnStashは壊せません"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null)
            return;

        Block block = e.getClickedBlock();

        if (!StashManager.isStashBlock(block.getLocation()))
            return;

        switch (block.getType()) {

            case CHEST,
                 TRAPPED_CHEST,
                 SHULKER_BOX,
                 WHITE_SHULKER_BOX,
                 ORANGE_SHULKER_BOX,
                 MAGENTA_SHULKER_BOX,
                 LIGHT_BLUE_SHULKER_BOX,
                 YELLOW_SHULKER_BOX,
                 LIME_SHULKER_BOX,
                 PINK_SHULKER_BOX,
                 GRAY_SHULKER_BOX,
                 LIGHT_GRAY_SHULKER_BOX,
                 CYAN_SHULKER_BOX,
                 PURPLE_SHULKER_BOX,
                 BLUE_SHULKER_BOX,
                 BROWN_SHULKER_BOX,
                 GREEN_SHULKER_BOX,
                 RED_SHULKER_BOX,
                 BLACK_SHULKER_BOX,
                 ENDER_CHEST,
                 SPAWNER -> {

                e.setCancelled(true);

                Player player = e.getPlayer();

                player.sendActionBar(
                        net.kyori.adventure.text.Component.text(
                                "§cこのSpawnStashは使用できません"));
            }
        }
    }
}