package toritori.tspawnstash.spawnstash;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import toritori.tspawnstash.Tspawnstash;
import toritori.tspawnstash.data.StructureBlock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;

public class SpawnStash5 {

    private static final Map<UUID, Location> pos1Map = new HashMap<>();
    private static final Map<UUID, Location> pos2Map = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    public static void init() {

        file = new File(
                Tspawnstash.getInstance().getDataFolder(),
                "structures.yml"
        );

        config = YamlConfiguration.loadConfiguration(file);
    }

    /*
        プレイヤー毎にコピーした建築を保存
     */
    private static final Map<UUID, List<StructureBlock>> structures =
            new HashMap<>();

    public static void setPos1(UUID uuid, Location loc) {
        pos1Map.put(uuid, loc.clone());
    }

    public static void setPos2(UUID uuid, Location loc) {
        pos2Map.put(uuid, loc.clone());
    }

    public static Location getPos1(UUID uuid) {
        return pos1Map.get(uuid);
    }

    public static Location getPos2(UUID uuid) {
        return pos2Map.get(uuid);
    }

    public static boolean hasSelection(UUID uuid) {
        return pos1Map.containsKey(uuid)
                && pos2Map.containsKey(uuid);
    }

    /**
     * 建築を保存
     */
    public static void loadStructure(Player player) {

        UUID uuid = player.getUniqueId();

        String path = uuid.toString();

        if (!config.contains(path))
            return;
        if (config.getConfigurationSection(path) == null)
            return;

        List<StructureBlock> blocks = new ArrayList<>();

        for (String key :
                config.getConfigurationSection(path)
                        .getKeys(false)) {

            int x = config.getInt(path + "." + key + ".x");
            int y = config.getInt(path + "." + key + ".y");
            int z = config.getInt(path + "." + key + ".z");

            Material material =
                    Material.valueOf(
                            config.getString(
                                    path + "." + key + ".material"));

            blocks.add(
                    new StructureBlock(
                            x,
                            y,
                            z,
                            material
                    ));
        }

        structures.put(uuid, blocks);
    }
    public static void saveStructure(Player player) {

        UUID uuid = player.getUniqueId();

        if (!hasSelection(uuid)) {
            player.sendMessage("§cPos1とPos2を設定してください。");
            return;
        }

        Location pos1 = getPos1(uuid);
        Location pos2 = getPos2(uuid);

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());

        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        // TPS保護
        int volume =
                (maxX - minX + 1)
                        * (maxY - minY + 1)
                        * (maxZ - minZ + 1);

        if (volume > 10000) {
            player.sendMessage(
                    "§c建築が大きすぎます。最大10000ブロックです。");
            return;
        }

        List<StructureBlock> blocks = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    Material material =
                            pos1.getWorld()
                                    .getBlockAt(x, y, z)
                                    .getType();

                    if (material == Material.AIR)
                        continue;

                    // Pos1基準の相対座標
                    blocks.add(new StructureBlock(
                            x - minX,
                            y - minY,
                            z - minZ,
                            material
                    ));
                }
            }
        }

        structures.put(uuid, blocks);

        // YAMLへ保存
        String path = uuid.toString();

        config.set(path, null);

        for (int i = 0; i < blocks.size(); i++) {

            StructureBlock block = blocks.get(i);

            String p = path + "." + i;

            config.set(p + ".x", block.getX());
            config.set(p + ".y", block.getY());
            config.set(p + ".z", block.getZ());
            config.set(p + ".material",
                    block.getMaterial().name());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage(
                "§a建築を保存しました！ §7("
                        + blocks.size()
                        + "ブロック)");
    }

    /**
     * /spawnstash 5
     */
    public static void toggle(Player player) {

        UUID uuid = player.getUniqueId();

        if (!structures.containsKey(uuid)) {
            loadStructure(player);
        }

        // 既存Stashや残骸があるなら削除して終了
        if (StashManager.toggle(player)) {
            return;
        }

        // まだ保存していない
        // 範囲選択されているなら常に最新の内容で保存
        if (hasSelection(uuid)) {

            saveStructure(player);

            // 保存失敗時
            if (!structures.containsKey(uuid)) {
                return;
            }

        } else if (!structures.containsKey(uuid)) {

            // 保存済みデータも選択範囲もない
            player.sendMessage("§c先に棒で範囲を選択してください。");
            return;
        }

            // 保存失敗時対策
            if (!structures.containsKey(uuid)) {
                return;
            }

        List<StructureBlock> blocks = structures.get(uuid);

        Location base =
                player.getLocation().getBlock().getLocation();

        StashManager.placeGradually(
                player,
                blocks,
                base
        );

        player.sendMessage("§aSpawnStashを生成しました。");
    }
    public static void clearCache(UUID uuid) {

        structures.remove(uuid);
        pos1Map.remove(uuid);
        pos2Map.remove(uuid);
    }
}