package co.riftmc.riftcore.NoTab;

import co.riftmc.riftcore.RiftCore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;

public class NoTab {

    public static void tabProtocol() {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(RiftCore.INSTANCE, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
            public void onPacketReceiving(final PacketEvent event) {
                try {
                    if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                        if (event.getPlayer().hasPermission("notab.bypass")) {
                            return;
                        }
                        final PacketContainer packet = event.getPacket();
                        final String message = ((String)packet.getSpecificModifier((Class)String.class).read(0)).toLowerCase();
                        if (message.startsWith("/") && !message.contains(" ")) {
                            event.setCancelled(true);
                        }
                    }
                }
                catch (Exception ex) {
                    RiftCore.INSTANCE.getLogger().severe("[NoTab] Error accessing to packet");
                }
            }
        });
    }

}
