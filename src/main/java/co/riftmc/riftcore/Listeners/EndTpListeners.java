package co.riftmc.riftcore.Listeners;

import co.riftmc.riftcore.RiftCore;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class EndTpListeners implements Listener {
    FileConfiguration fileConfiguration = RiftCore.INSTANCE.getConfig();

    private List<String> worlds = fileConfiguration.getStringList("EndTp.Whitelisted-TPWorlds");
    private List<String> commands = fileConfiguration.getStringList("EndTp.CommandsToRun");


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPortal(PlayerPortalEvent event){
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        if (worlds.contains(event.getFrom().getWorld().getName())){
            Player player = event.getPlayer();
            event.setCancelled(true);
            for (String command : commands){
                String newcommand = command.replaceAll("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender() , newcommand);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEXPPortal(PlayerPortalEvent event){
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        if (event.getFrom().getWorld().getName().equals("Expedition")){
            Player player = event.getPlayer();
            event.setCancelled(true);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender() , "mv tp " + player.getName() + " Expedition_The_End");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onblockevent(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType().toString().equals("ENDER_PORTAL_FRAME")){
            ItemStack itemStack = CraftItemStack.asNMSCopy(event.getItemInHand());
            NBTTagCompound compound = (itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound();
            if (compound.hasKey("riftgenerators")) return;
            Location loc = event.getBlock().getLocation();
            World world = ((CraftWorld) event.getBlock().getWorld()).getHandle();
            BlockPosition blockposition = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
            IBlockData iblockdata = world.getType(blockposition);
            world.setTypeAndData(blockposition, iblockdata.set(BlockEnderPortalFrame.EYE, Boolean.valueOf(true)), 2);
            world.updateAdjacentComparators(blockposition, Blocks.END_PORTAL_FRAME);
            EnumDirection enumdirection1 = (EnumDirection) iblockdata.get(BlockEnderPortalFrame.FACING);
            int j = 0;
            int k = 0;
            boolean flag = false;
            boolean flag1 = true;
            EnumDirection enumdirection2 = enumdirection1.e();

            for (int l = -2; l <= 2; ++l) {
                BlockPosition blockposition1 = blockposition.shift(enumdirection2, l);
                IBlockData iblockdata1 = world.getType(blockposition1);

                if (iblockdata1.getBlock() == Blocks.END_PORTAL_FRAME) {
                    if (!((Boolean) iblockdata1.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                        flag1 = false;
                        break;
                    }

                    k = l;
                    if (!flag) {
                        j = l;
                        flag = true;
                    }
                }
            }

            if (flag1 && k == j + 2) {
                BlockPosition blockposition2 = blockposition.shift(enumdirection1, 4);

                int i1;

                for (i1 = j; i1 <= k; ++i1) {
                    BlockPosition blockposition3 = blockposition2.shift(enumdirection2, i1);
                    IBlockData iblockdata2 = world.getType(blockposition3);

                    if (iblockdata2.getBlock() != Blocks.END_PORTAL_FRAME || !((Boolean) iblockdata2.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                        flag1 = false;
                        break;
                    }
                }

                int j1;
                BlockPosition blockposition4;

                for (i1 = j - 1; i1 <= k + 1; i1 += 4) {
                    blockposition2 = blockposition.shift(enumdirection2, i1);

                    for (j1 = 1; j1 <= 3; ++j1) {
                        blockposition4 = blockposition2.shift(enumdirection1, j1);
                        IBlockData iblockdata3 = world.getType(blockposition4);

                        if (iblockdata3.getBlock() != Blocks.END_PORTAL_FRAME || !((Boolean) iblockdata3.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                            flag1 = false;
                            break;
                        }
                    }
                }

                if (flag1) {
                    for (i1 = j; i1 <= k; ++i1) {
                        blockposition2 = blockposition.shift(enumdirection2, i1);

                        for (j1 = 1; j1 <= 3; ++j1) {
                            blockposition4 = blockposition2.shift(enumdirection1, j1);
                            world.setTypeAndData(blockposition4, Blocks.END_PORTAL.getBlockData(), 2);
                        }
                    }
                }
            }
        }
    }
}
