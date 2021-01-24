package co.riftmc.riftcore.Listeners;

import co.riftmc.riftcore.RiftCore;
import co.riftmc.riftcore.Utils.ItemBuilder;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.wildstacker.api.events.BarrelDropEvent;
import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnerPlaceListener implements Listener {
    RiftCore riftCore;
    FileConfiguration config;
    List commonSpawners;
    List uncommonSpawners;
    List rareSpawners;
    List epicSpawners;
    String commonDeny;
    String uncommonDeny;
    String rareDeny;
    String epicDeny;
    public SpawnerPlaceListener(RiftCore riftCore) {
        this.riftCore = riftCore;
        this.config = riftCore.getConfig();
        commonSpawners = config.getStringList("SpawnerPlacing.Common-Mobs").stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        uncommonSpawners = config.getStringList("SpawnerPlacing.Uncommon-Mobs").stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        rareSpawners = config.getStringList("SpawnerPlacing.Rare-Mobs").stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        epicSpawners = config.getStringList("SpawnerPlacing.Epic-Mobs").stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        commonDeny = config.getString("SpawnerPlacing.Common-Mobs-Deny");
        uncommonDeny = config.getString("SpawnerPlacing.Uncommon-Mobs-Deny");
        rareDeny = config.getString("SpawnerPlacing.Rare-Mobs-Deny");
        epicDeny = config.getString("SpawnerPlacing.Epic-Mobs-Deny");
    }

    @EventHandler
    public void onPlace2(SpawnerPlaceEvent event){
        Island island = SuperiorSkyblockAPI.getIslandAt(event.getSpawner().getLocation());
        if (island == null) return;
        Player player = event.getPlayer();
        StackedSpawner spawner = event.getSpawner();

        EntityType le = spawner.getSpawnedType();

        String mob = le.toString();
        if (commonSpawners.contains(mob)){
            if (island.getOwner().isOnline() ? !island.getOwner().hasPermission("shopguiplus.item.commonspawners.*") : !player.hasPermission("shopguiplus.item.commonspawners.*")){
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', commonDeny));
                return;
            }
            return;
        }
        if (uncommonSpawners.contains(mob)){
            if (island.getOwner().isOnline() ? !island.getOwner().hasPermission("shopguiplus.item.uncommonspawners.*") : !player.hasPermission("shopguiplus.item.uncommonspawners.*")){
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', uncommonDeny));
                return;
            }
            return;
        }
        if (rareSpawners.contains(mob)){
            if (island.getOwner().isOnline() ? !island.getOwner().hasPermission("shopguiplus.item.rarespawners.*") : !player.hasPermission("shopguiplus.item.rarespawners.*")){
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', rareDeny));
                return;
            }
            return;
        }
        if (epicSpawners.contains(mob)){
            if (island.getOwner() != null ? !island.getOwner().hasPermission("shopguiplus.item.epicspawners.*") : !player.hasPermission("shopguiplus.item.epicspawners.*")){
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', epicDeny));
                return;
            }
            return;
        }


    }

    @EventHandler
    public void onBarrelDrop(BarrelDropEvent e){
        ItemStack barrel = e.getItemStack();

        if(barrel.getType() == Material.SLIME_BLOCK){
            Bukkit.broadcastMessage("WORKS");
            ItemStack item = new ItemBuilder(Material.SLIME_BLOCK)
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSlime Trophy"))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&f500 Points"))
                    .addEnchant(Enchantment.SILK_TOUCH,1)
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .build();
            item.setAmount(barrel.getAmount());
            e.setItemStack(item);
        }

        if(barrel.getType() == Material.NOTE_BLOCK){
            ItemStack item = new ItemBuilder(Material.NOTE_BLOCK)
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lNoteblock Trophy"))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&f1000 Points"))
                    .addEnchant(Enchantment.SILK_TOUCH,1)
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .build();
            item.setAmount(barrel.getAmount());
            e.setItemStack(item);
        }
        if(barrel.getType() == Material.DRAGON_EGG){
            ItemStack item = new ItemBuilder(Material.DRAGON_EGG)
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lDragon Trophy"))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&f6000 Points"))
                    .addEnchant(Enchantment.SILK_TOUCH,1)
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .build();
            item.setAmount(barrel.getAmount());
            e.setItemStack(item);
        }


    }


}
