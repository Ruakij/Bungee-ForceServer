package eu.ruekov.ruakij.forceServer.listener;

import eu.ruekov.ruakij.forceServer.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Logger;

public class PostLogin implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {

        ProxiedPlayer p = e.getPlayer();

        // byUUID
        String forcedTargetServer = Main.forceServerByUUID.get(p.getUniqueId().toString());
        // byName
        if(forcedTargetServer == null) Main.forceServerByName.get(p.getName());
        // byPermission
        if(forcedTargetServer == null) {
            for(String permission : Main.forceServerByPermission.keySet()) {
                if(p.hasPermission(permission)){
                    forcedTargetServer = Main.forceServerByPermission.get(permission);
                }
            }
            Main.forceServerByPermission.get(p);
        }
        if(forcedTargetServer == null) return;

        ServerInfo sInfo = BungeeCord.getInstance().getServerInfo(forcedTargetServer);
        if(sInfo == null) {
            Main.log.severe("TargetServer '"+ forcedTargetServer +"' not found!");
            return;
        }

        p.connect(sInfo);
    }
}
