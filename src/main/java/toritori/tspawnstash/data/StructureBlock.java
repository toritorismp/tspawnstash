package toritori.tspawnstash.data;

import org.bukkit.Material;

public class StructureBlock {

    private final int x;
    private final int y;
    private final int z;
    private final Material material;

    public StructureBlock(int x, int y, int z, Material material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getMaterial() {
        return material;
    }
}