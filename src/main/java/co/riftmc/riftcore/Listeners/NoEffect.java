package co.riftmc.riftcore.Listeners;

import co.riftmc.riftcore.RiftCore;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;

import java.util.List;

public class NoEffect implements Listener {

    FileConfiguration fileConfiguration = RiftCore.INSTANCE.getConfig();
    List<Integer> materials = RiftCore.INSTANCE.getConfig().getIntegerList("NoBurn.Items-That-Dont-Burn");
    @EventHandler
    public void onBurn(EntityCombustEvent event){
        if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.SKELETON)event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVilly(EntityDamageEvent event){
        String world = event.getEntity().getWorld().getName();
        if (world.equals("Expedition") && event.getEntity().getType() == EntityType.VILLAGER){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noItemBurn(final EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.DROPPED_ITEM && e.getEntity() instanceof Item) {
            if (e.getCause() == EntityDamageEvent.DamageCause.LAVA){
                if (materials.contains(((Item)e.getEntity()).getItemStack().getTypeId())){
                    e.setCancelled(true);
                    e.getEntity().setInvulnerable(true);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onFoodLevelChange(final FoodLevelChangeEvent foodLevelChangeEvent) {
        if (foodLevelChangeEvent.getFoodLevel() == 30)return;
        if (!this.fileConfiguration.getBoolean("DisableHunger.Enable")) {
            return;
        }
        final List<String> worlds = fileConfiguration.getStringList("DisableHunger.Worlds");
        if (worlds.contains(foodLevelChangeEvent.getEntity().getWorld().getName())) {
            return;
        }
        if (!(foodLevelChangeEvent.getEntity() instanceof Player)) {
            return;
        }
        if (this.fileConfiguration.getBoolean("DisableHunger.Needs-Permission")) {
            final Player player = (Player)foodLevelChangeEvent.getEntity();
            if (player.hasPermission("NoHunger.Use")) {
                foodLevelChangeEvent.setCancelled(true);
            }
        }
        else {
            foodLevelChangeEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void miniSlime(CreatureSpawnEvent event){
        if (event.getEntityType() == EntityType.SLIME){
            Slime slime = (Slime)event.getEntity();
            slime.setSize(1);
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (!fileConfiguration.getBoolean("ThunderDeath.Enable"))return;
        if (event.getEntity() instanceof Player) {
            Player player = event.getEntity();
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
    }

    @EventHandler
    public void signFix(final SignChangeEvent event) {
        if (event.getLine(0).equals("r")) {
            event.setLine(0, "");
        }
        if (event.getLine(1).equals("r")) {
            event.setLine(1, "");
        }
        if (event.getLine(2).equals("r")) {
            event.setLine(2, "");
        }
        if (event.getLine(3).equals("r")) {
            event.setLine(3, "");
        }
    }

    @EventHandler
    public void waterToCrop(BlockFromToEvent event){
        if (event.getToBlock().getType() == Material.CROPS || event.getToBlock().getType() == Material.POTATO || event.getToBlock().getType() == Material.CARROT || event.getToBlock().getType() == Material.NETHER_WARTS){
            event.setCancelled(true);
        }
    }
}
