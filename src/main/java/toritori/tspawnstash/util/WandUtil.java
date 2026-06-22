package toritori.tspawnstash.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import toritori.tspawnstash.Tspawnstash;

public class WandUtil {

    private static final NamespacedKey KEY =
            new NamespacedKey(Tspawnstash.getInstance(), "wand");

    public static ItemStack createWand() {

        ItemStack stick = new ItemStack(Material.STICK);

        ItemMeta meta = stick.getItemMeta();

        meta.setDisplayName("§dSpawnStash Wand");

        meta.getPersistentDataContainer().set(
                KEY,
                PersistentDataType.BOOLEAN,
                true
        );

        stick.setItemMeta(meta);

        return stick;
    }

    public static boolean isWand(ItemStack item) {

        if (item == null)
            return false;

        if (!item.hasItemMeta())
            return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(KEY, PersistentDataType.BOOLEAN);
    }
}