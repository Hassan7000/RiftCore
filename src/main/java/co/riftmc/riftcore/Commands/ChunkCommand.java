package co.riftmc.riftcore.Commands;

import co.riftmc.riftcore.RiftCore;
import co.riftmc.riftcore.Utils.ParticleEffect;
import co.riftmc.riftcore.Utils.VisualizeUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;

public class ChunkCommand implements Listener {
    private static final String ARGS_SEPARATOR = " ";
    private static final String[] ASB_COMMAND_ALIASES = {"is", "island"};
    private static final String[] ARGS_TO_OVERRIDE = {"sc", "seechunk"};
    public static HashMap<String, Boolean> seeChunkMap = new HashMap<>();
    Long interval = 10L;
    private boolean useParticles = RiftCore.INSTANCE.getConfig().getBoolean("SeeChunk.Use-Particles");
    private ParticleEffect effect = ParticleEffect.BARRIER;
    private int taskID = -1;
    private String disabledMessage = format(RiftCore.INSTANCE.getConfig().getString("SeeChunk.Disabled-Message"));
    private String enabledMessage = format(RiftCore.INSTANCE.getConfig().getString("SeeChunk.Enabled-Message"));


    public String format(String raw) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', raw);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent e){
        seeChunkMap.remove(e.getPlayer().getName());
    }

    public boolean parseCommand(Player player, String label) {
        RiftCore.INSTANCE.getConfig();
        String[] args = label.split(ARGS_SEPARATOR);
        String cmd = args[0];
        if (Arrays.stream(ASB_COMMAND_ALIASES).noneMatch(asbCommand -> asbCommand.equalsIgnoreCase(cmd))) return false;
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length == 0) return false;

        String finalCmd = args[0];
        if (Arrays.stream(ARGS_TO_OVERRIDE).noneMatch(arg -> arg.equalsIgnoreCase(finalCmd))) return false;
        if(finalCmd.equalsIgnoreCase("sc") || finalCmd.equalsIgnoreCase("seechunk")) {
            if(args.length == 1) {
                if (seeChunkMap.containsKey(player.getName())) {
                    seeChunkMap.remove(player.getName());
                    player.sendMessage(disabledMessage);
                    return true;

                } else {
                    seeChunkMap.put(player.getName(), true);
                    player.sendMessage(enabledMessage);
                    manageTask();
                    return true;
                }
            }
        }
        return false;
    }

    private void manageTask() {
        if (taskID != -1) {
            if (seeChunkMap.keySet().size() == 0) {
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = -1;
            }
        } else {
            startTask();
        }
    }

    private void startTask() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(RiftCore.INSTANCE, () -> {
            for (Object nameObject : seeChunkMap.keySet()) {
                String name = nameObject + "";
                Player player = Bukkit.getPlayer(name);
                showBorders(player);
            }
            manageTask();
        }, 0, interval);
    }

    private void showBorders(Player me) {
        if (!me.isOnline()){
            seeChunkMap.remove(me.getName());
            return;
        }
        World world = me.getWorld();
        Chunk location = me.getLocation().getChunk();
        int chunkX = location.getX();
        int chunkZ = location.getZ();

        int blockX;
        int blockZ;

        blockX = chunkX * 16;
        blockZ = chunkZ * 16;
        showPillar(me, world, blockX, blockZ);


        blockX = chunkX * 16 + 15;
        blockZ = chunkZ * 16;
        showPillar(me, world, blockX, blockZ);

        blockX = chunkX * 16;
        blockZ = chunkZ * 16 + 15;
        showPillar(me, world, blockX, blockZ);

        blockX = chunkX * 16 + 15;
        blockZ = chunkZ * 16 + 15;
        showPillar(me, world, blockX, blockZ);
    }

    private void showPillar(Player player, World world, int blockX, int blockZ) {
        for (int blockY = 0; blockY < player.getLocation().getBlockY() + 30; blockY++) {
            Location loc = new Location(world, blockX, blockY, blockZ).add(0.5, 0, 0.5);
            if (loc.getBlock().getType() != Material.AIR) {
                continue;
            }
            if (useParticles) {
                this.effect.display(0, 0, 0, 0, 1, loc, player);
            } else {
                Material type = blockY % 5 == 0 ? Material.REDSTONE_LAMP_ON : Material.STAINED_GLASS;
                VisualizeUtil.addLocation(player, loc, type);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String label = e.getMessage().substring(1);
        boolean parsed = parseCommand(player, label);
        if (parsed)
            e.setCancelled(true);
    }

}

