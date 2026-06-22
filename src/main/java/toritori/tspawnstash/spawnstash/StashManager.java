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

        // 1個でも残っていたら存在する
        for (Location loc : placedBlocks.get(uuid)) {

            if (loc.getBlock().getType() != Material.AIR)
                return true;
        }

        return false;
    }

    /*
        ブロック設置
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
    public static void place(Player player,
                             Location loc,
                             Material material) {

        placedBlocks
                .computeIfAbsent(
                        player.getUniqueId(),
                        k -> new HashSet<>())
                .add(loc.clone());

        Bukkit.getRegionScheduler().run(
                Tspawnstash.getInstance(),
                loc,
                task -> {

                    // 埋まっていても強制除去
                    loc.getBlock().setType(Material.AIR, false);

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

        for (Location loc : blocks) {

            Bukkit.getRegionScheduler().run(
                    Tspawnstash.getInstance(),
                    loc,
                    task -> loc.getBlock()
                            .setType(Material.AIR, false));
        }

        placedBlocks.remove(uuid);
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

        UUID uuid = player.getUniqueId();

        if (!placedBlocks.containsKey(uuid))
            return;

        for (Location loc : placedBlocks.get(uuid)) {

            Bukkit.getRegionScheduler().run(
                    Tspawnstash.getInstance(),
                    loc,
                    task -> loc.getBlock()
                            .setType(Material.AIR, false));
        }

        placedBlocks.remove(uuid);
    }

    /*
        トグル処理
     */
    public static boolean toggle(Player player) {

        // 既に存在する
        if (hasStash(player)) {

            remove(player);
            player.sendMessage("§cSpawnStashを削除しました");
            return true;
        }

        // 残骸がある
        if (hasRemains(player)) {

            cleanup(player);

            player.sendMessage("§e残骸を削除しました");
            return true;
        }

        return false;
    }
}