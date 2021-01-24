package co.riftmc.riftcore.Listeners;

import co.riftmc.riftcore.RiftCore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.Flag;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.*;

public class RankBoosterListener implements Listener {

    HashMap<String, Double> xpBoosterRank = RiftCore.INSTANCE.getXpBoosterRank();

    @EventHandler
    public void onXp(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        LuckPerms luckPerms = LuckPermsProvider.get();
        User target = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        QueryOptions queryOptions = target.getQueryOptions().toBuilder()
                .flag(Flag.RESOLVE_INHERITANCE, false)
                .build();
        Collection<Group> groups = target.getInheritedGroups(queryOptions);
        double multiplier = 1;
        for (String group : xpBoosterRank.keySet()){
            if (groups.contains(luckPerms.getGroupManager().getGroup(group))){
                multiplier = xpBoosterRank.get(group);
                break;
            }
        }
        event.setAmount((int) (event.getAmount() * multiplier));

    }

}
