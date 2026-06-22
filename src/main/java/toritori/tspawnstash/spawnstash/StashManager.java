package toritori.tspawnstash.spawnstash;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import toritori.tspawnstash.Tspawnstash;
import toritori.tspawnstash.data.StructureBlock;

import java.util.*;

public class StashManager {

    /*
        プレイヤー毎に設置したブロックを保存
     */
    private static final Map<UUID, Set<Location>> placedBlocks =
            new HashMap<>();

    /*
        設置前のブロックを保存
     */
    private static final Map<UUID, Map<Location, Material>> originalBlocks =
            new HashMap<>();

    /*
        SpawnStashのブロックか判定
     */
    public static boolean isStashBlock(Location loc) {

        for (Set<Location> set : placedBlocks.values()) {

            for (Location l : set) {

                if (l.equals(loc))
                    return true;
            }
        }

        return false;
    }

    /*
        Stashが存在するか
     */
    public static boolean hasStash(Player player) {

        UUID uuid = player.getUniqueId();

        if (!placedBlocks.containsKey(uuid))
            return false;

        for (Location loc : placedBlocks.get(uuid)) {

            if (loc.getBlock().getType() != Material.AIR)
                return true;
        }

        return false;
    }

    /*
        ブロックを少しずつ設置
     */
    public static void placeGradually(
            Player player,
            List<StructureBlock> blocks,
            Location base) {

        Iterator<StructureBlock> iterator =
                blocks.iterator();

        Bukkit.getGlobalRegionScheduler()
                .runAtFixedRate(
                        Tspawnstash.getInstance(),

                        task -> {

                            int count = 0;

                            while (iterator.hasNext()
                                    && count < 20) {

                                StructureBlock block =
                                        iterator.next();

                                Location loc =
                                        base.clone().add(
                                                block.getX(),
                                                block.getY(),
                                                block.getZ()
                                        );

                                place(
                                        player,
                                        loc,
                                        block.getMaterial()
                                );

                                count++;
                            }

                            if (!iterator.hasNext()) {
                                task.cancel();
                            }

                        },

                        1L,
                        1L
                );
    }

    /*
        ブロック設置
     */
    public static void place(Player player,
                             Location loc,
                             Material material) {

        UUID uuid = player.getUniqueId();

        placedBlocks
                .computeIfAbsent(
                        uuid,
                        k -> new HashSet<>())
                .add(loc.clone());

        Bukkit.getRegionScheduler().run(
                Tspawnstash.getInstance(),
                loc,
                task -> {

                    // 初回のみ元ブロックを保存
                    originalBlocks
                            .computeIfAbsent(
                                    uuid,
                                    k -> new HashMap<>())
                            .putIfAbsent(
                                    loc.clone(),
                                    loc.getBlock().getType()
                            );

                    loc.getBlock().setType(material, false);
                });
    }

    /*
        完全削除
     */
    public static void remove(Player player) {

        UUID uuid = player.getUniqueId();

        if (!placedBlocks.containsKey(uuid))
            return;

        Set<Location> blocks = placedBlocks.get(uuid);

        Map<Location, Material> originals =
                originalBlocks.get(uuid);

        for (Location loc : blocks) {

            Bukkit.getRegionScheduler().run(
                    Tspawnstash.getInstance(),
                    loc,
                    task -> {

                        Material original = Material.AIR;

                        if (originals != null) {
                            original = originals.getOrDefault(
                                    loc,
                                    Material.AIR
                            );
                        }

                        loc.getBlock()
                                .setType(original, false);
                    });
        }

        placedBlocks.remove(uuid);
        originalBlocks.remove(uuid);
    }

    /*
        残骸があるか
     */
    public static boolean hasRemains(Player player) {

        UUID uuid = player.getUniqueId();

        if (!placedBlocks.containsKey(uuid))
            return false;

        for (Location loc : placedBlocks.get(uuid)) {

            if (loc.getBlock().getType() != Material.AIR)
                return true;
        }

        return false;
    }

    /*
        残骸を掃除
     */
    public static void cleanup(Player player) {

        remove(player);
    }

    /*
        トグル処理
     */
    public static boolean toggle(Player player) {

        if (hasStash(player)) {

            remove(player);
            player.sendMessage("§cSpawnStashを削除しました");
            return true;
        }

        if (hasRemains(player)) {

            cleanup(player);
            player.sendMessage("§e残骸を削除しました");
            return true;
        }

        return false;
    }
}