package toritori.tspawnstash.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import toritori.tspawnstash.spawnstash.SpawnStash5;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        UUID uuid = e.getPlayer().getUniqueId();

        SpawnStash5.clearCache(uuid);
    }
}