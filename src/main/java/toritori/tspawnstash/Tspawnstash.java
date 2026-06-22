package toritori.tspawnstash;

import org.bukkit.plugin.java.JavaPlugin;
import toritori.tspawnstash.commands.SpawnStashCommand;
import toritori.tspawnstash.listeners.PlayerListener;
import toritori.tspawnstash.listeners.StashProtectListener;
import toritori.tspawnstash.listeners.WandListener;
import toritori.tspawnstash.spawnstash.SpawnStash5;

import java.io.File;

public class Tspawnstash extends JavaPlugin {

    private static Tspawnstash instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        // structures.yml作成
        if (!new File(getDataFolder(), "structures.yml").exists()) {
            saveResource("structures.yml", false);
        }

        // SpawnStash5初期化
        SpawnStash5.init();

        // コマンド
        SpawnStashCommand command = new SpawnStashCommand();

        getCommand("spawnstash").setExecutor(command);
        getCommand("spawnstash").setTabCompleter(command);

        // イベント登録
        getServer().getPluginManager()
                .registerEvents(new WandListener(), this);

        getServer().getPluginManager()
                .registerEvents(new StashProtectListener(), this);

        getLogger().info("Tspawnstash has been enabled!");
        getServer().getPluginManager()
                .registerEvents(new PlayerListener(), this);
    }

    public static Tspawnstash getInstance() {
        return instance;
    }
}