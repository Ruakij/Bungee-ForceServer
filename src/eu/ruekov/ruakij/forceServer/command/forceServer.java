package eu.ruekov.ruakij.forceServer.command;

import eu.ruekov.ruakij.forceServer.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class forceServer extends Command {
    public forceServer(String name) { super(name); }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // Permission check if sender is player
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer)sender;

            if(!p.hasPermission("forceServer.cmd")){
                p.sendMessage("§cNo Permission!");
                return;
            }
        }

        Main.log.info("Reloading config..");
        sender.sendMessage("§eReloading..");
        try {
            Main.plugin.loadConfig();
        } catch (IOException ex) {
            Main.log.severe("Reloading config failed!");
            ex.printStackTrace();

            sender.sendMessage("§cFailed! See config for details");
            return;
        }
        sender.sendMessage("§aDone");
    }
}
