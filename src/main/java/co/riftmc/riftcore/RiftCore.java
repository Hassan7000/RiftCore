package co.riftmc.riftcore;

import co.riftmc.riftcore.Commands.ChunkCommand;
import co.riftmc.riftcore.Listeners.*;
import co.riftmc.riftcore.NoTab.NoTab;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public final class RiftCore extends JavaPlugin {

    public static RiftCore INSTANCE;
    FileConfiguration fileConfiguration = getConfig();
    HashMap<String, Double> xpBoosterRank = new HashMap<>();

    @Override
    public void onEnable() {
        this.INSTANCE = this;

//        Config Stuff
        loadConfigs();
        fileConfiguration.options().copyDefaults(true);
        saveConfig();

//        Commands


//        Listeners
        Bukkit.getPluginManager().registerEvents(new NoEffect(), this);
        Bukkit.getPluginManager().registerEvents(new RankBoosterListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkCommand(), this);
        Bukkit.getPluginManager().registerEvents(new EndTpListeners(), this);
        Bukkit.getPluginManager().registerEvents(new BlockSyntax(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerPlaceListener(this), this);
        NoTab.tabProtocol();

    }

    private void loadConfigs() {
        fileConfiguration.addDefault("DisableHunger.Enable", true);
        fileConfiguration.addDefault("DisableHunger.Worlds", Arrays.asList("Expedition", "Expedition_the_end", "Expedition_nether"));
        fileConfiguration.addDefault("DisableHunger.Needs-Permission", true);

        fileConfiguration.addDefault("NoBurn.Items-That-Dont-Burn", Arrays.asList(52, 339, 399, 421, 383, 381, 388));

        fileConfiguration.addDefault("SeeChunk.Use-Particles", true);
        fileConfiguration.addDefault("SeeChunk.Enabled-Message", "&d&lRift&5&lMC &8>> &dSeechunk Enabled");
        fileConfiguration.addDefault("SeeChunk.Disabled-Message", "&d&lRift&5&lMC &8>> &dSeechunk Disabled");

        fileConfiguration.addDefault("ThunderDeath.Enable", Boolean.valueOf(true));

        fileConfiguration.addDefault("EndTp.Whitelisted-TPWorlds", Arrays.asList("Islands"));
        fileConfiguration.addDefault("EndTp.CommandsToRun", Arrays.asList("mv tp %player% Outpost"));

        fileConfiguration.addDefault("BlockSyntax.invalidCommandMessage", "&d&lRift&5&lMC &8>> &dUnknown command. Try /help");
        fileConfiguration.addDefault("BlockSyntax.whitelistedPlugins", Arrays.asList("superiorskyblock2","guiredeemmcmmo","stafftools","chatsentry"));

        fileConfiguration.addDefault("RankBoosters.xp", ImmutableMap.of("rift+", 2.25, "rift", 2.00, "lunar", 1.75, "eclipse", 1.50, "nova", 1.25));

        fileConfiguration.addDefault("SpawnerPlacing.Common-Mobs", Arrays.asList("Chicken", "Sheep", "Pig", "Cow", "Rabbit", "Horse", "Zombie"));
        fileConfiguration.addDefault("SpawnerPlacing.Uncommon-Mobs", Arrays.asList("Skeleton", "Spider", "Cave_Spider", "Slime", "Blaze"));
        fileConfiguration.addDefault("SpawnerPlacing.Rare-Mobs", Arrays.asList("Creeper", "Pig_Zombie", "Enderman", "Guardian", "Iron_Golem"));
        fileConfiguration.addDefault("SpawnerPlacing.Epic-Mobs", Arrays.asList("Villager", "Ghast", "Witch"));
        fileConfiguration.addDefault("SpawnerPlacing.Common-Mobs-Deny", "&cYou require a higher Island Level to access Common Spawners");
        fileConfiguration.addDefault("SpawnerPlacing.Uncommon-Mobs-Deny", "&cYou require a higher Island Level to access Uncommon Spawners");
        fileConfiguration.addDefault("SpawnerPlacing.Rare-Mobs-Deny", "&cYou require a higher Island Level to access Rare Spawners");
        fileConfiguration.addDefault("SpawnerPlacing.Epic-Mobs-Deny", "&cYou require a higher Island Level to access Epic Spawners");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for(String rank : getConfig().getConfigurationSection("RankBoosters.xp").getKeys(false)) {
            xpBoosterRank.put(rank, Double.valueOf(getConfig().getString("RankBoosters.xp." + rank)));
        }
    }

    public HashMap<String, Double> getXpBoosterRank(){
        return xpBoosterRank;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
