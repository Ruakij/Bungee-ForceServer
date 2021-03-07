package eu.ruekov.ruakij.forceServer;

import eu.ruekov.ruakij.forceServer.command.forceServer;
import eu.ruekov.ruakij.forceServer.listener.PostLogin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.config.Configuration;


import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class Main extends Plugin {

    public static Main plugin;
    public static PluginDescription pInfo;

    public static Logger log;

    public static Configuration config;

    public static HashMap<String, String> forceServerByUUID;
    public static HashMap<String, String> forceServerByName;
    public static HashMap<String, String> forceServerByPermission;

    public void onEnable() {
        log = getLogger(); // Get logger
        plugin = this; // Get plugin
        pInfo = this.getDescription(); // Get plugin-info

        log.info("Loading config..");
        try {
            loadConfig();
        } catch (IOException ex) {
            log.severe("Loading config failed!");
            ex.printStackTrace();

            // Disable plugin
            onDisable();
            return;
        }

        log.info("Registering commands..");
        this.getProxy().getPluginManager().registerCommand(this, new forceServer("forceserver"));

        log.info("Registering events..");
        this.getProxy().getPluginManager().registerListener(this, new PostLogin());
    }

    @Override
    public void onDisable() {

        log.info("Version: " + pInfo.getVersion() + " is now disabled!");
    }

    public void loadConfig() throws IOException {
        config = ConfigHelper.loadOrRecreateConfig(this, "config.yml");

        // Load UUID-List into memory
        forceServerByUUID = new HashMap<>();
        Configuration byUuidSection = config.getSection("forceServer.byUUID");
        for(String uuid : byUuidSection.getKeys()) {
            String targetServer = byUuidSection.getString(uuid);
            forceServerByUUID.put(uuid, targetServer);
        }
        log.info(forceServerByUUID.size() +" UUID's");

        forceServerByName = new HashMap<>();
        Configuration byNameSection = config.getSection("forceServer.byName");
        for(String uuid : byNameSection.getKeys()) {
            String targetServer = byNameSection.getString(uuid);
            forceServerByName.put(uuid, targetServer);
        }
        log.info(forceServerByName.size() +" Names");

        forceServerByPermission = new HashMap<>();
        Configuration byPermissionSection = config.getSection("forceServer.byPermission");
        for(String uuid : byPermissionSection.getKeys()) {
            String targetServer = byPermissionSection.getString(uuid);
            forceServerByPermission.put(uuid, targetServer);
        }
        log.info(forceServerByPermission.size() +" Permission-Nodes");
    }
}
