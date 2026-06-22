package toritori.tspawnstash.spawnstash;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SpawnStash {

    public static void toggle(Player player, int type) {

        if (StashManager.toggle(player))
            return;

        Location base = player.getLocation().getBlock().getLocation();

        switch (type) {
            case 1 -> build1(player, base);
            case 2 -> build2(player, base);
            case 3 -> build3(player, base);
            case 4 -> build4(player, base);
        }
    }

    private static void build1(Player player, Location base) {

        StashManager.place(player,
                base.clone(),
                Material.SPAWNER);

        StashManager.place(player,
                base.clone().add(0, 1, 0),
                Material.SMALL_AMETHYST_BUD);
    }

    private static void build2(Player player, Location base) {

        for (int x = 0; x < 2; x++) {

            StashManager.place(player,
                    base.clone().add(x, 0, 0),
                    Material.SPAWNER);

            StashManager.place(player,
                    base.clone().add(x, 1, 0),
                    Material.CHEST);
        }
    }

    private static void build3(Player player, Location base) {

        for (int x = 0; x < 3; x++) {

            StashManager.place(player,
                    base.clone().add(x, 1, 0),
                    Material.CHEST);
        }

        StashManager.place(player,
                base.clone(),
                Material.SPAWNER);

        StashManager.place(player,
                base.clone().add(1, 0, 0),
                Material.SHULKER_BOX);

        StashManager.place(player,
                base.clone().add(2, 0, 0),
                Material.SHULKER_BOX);
    }

    private static void build4(Player player, Location base) {

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {

                    boolean wall =
                            x == 0 || x == 4 ||
                                    y == 0 || y == 4 ||
                                    z == 0 || z == 4;

                    if (wall) {
                        StashManager.place(player,
                                base.clone().add(x, y, z),
                                Material.STONE);
                    } else {
                        StashManager.place(player,
                                base.clone().add(x, y, z),
                                Material.AIR);
                    }
                }
            }
        }

        StashManager.place(player,
                base.clone().add(1, 1, 1),
                Material.CHEST);

        StashManager.place(player,
                base.clone().add(3, 1, 1),
                Material.CHEST);

        StashManager.place(player,
                base.clone().add(1, 1, 3),
                Material.SPAWNER);

        StashManager.place(player,
                base.clone().add(3, 1, 3),
                Material.ENDER_CHEST);
    }
}