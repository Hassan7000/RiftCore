package co.riftmc.riftcore.Listeners;

import co.riftmc.riftcore.RiftCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getServer;

public class BlockSyntax implements Listener {
    private String invalidCommandMessage = ChatColor.translateAlternateColorCodes('&', RiftCore.INSTANCE.getConfig().getString("BlockSyntax.invalidCommandMessage"));

    private List<String> whitelistedPlugins = RiftCore.INSTANCE.getConfig().getStringList("BlockSyntax.whitelistedPlugins");

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        Pattern p = Pattern.compile("^/([a-zA-Z0-9_]+):");
        Matcher m = p.matcher(e.getMessage());
        if (!m.find())
            return;
        String pluginRef = m.group(1);
        if (this.whitelistedPlugins.contains(pluginRef))
            return;
        if (pluginRef.equalsIgnoreCase("bukkit") || pluginRef.equalsIgnoreCase("minecraft")) {
            e.getPlayer().sendMessage(this.invalidCommandMessage);
            e.setCancelled(true);
        } else {
            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
                if (plugin.getName().equalsIgnoreCase(pluginRef)) {
                    e.getPlayer().sendMessage(this.invalidCommandMessage);
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }
}
