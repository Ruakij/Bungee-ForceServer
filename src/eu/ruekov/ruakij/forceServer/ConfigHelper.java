package eu.ruekov.ruakij.forceServer;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.Random;
import java.util.logging.Logger;

public class ConfigHelper {
    public static Configuration loadOrRecreateConfig(Plugin plugin, String resource) throws IOException {
        return loadOrRecreateConfig(plugin, resource, null);
    }
    public static Configuration loadOrRecreateConfig(Plugin plugin, String resource, Logger log) throws IOException {

        for(int i=0; i<2; i++) {
            try {
                // Copy default config if not existing
                ConfigHelper.loadAndSaveResource(plugin, "config.yml");
            } catch (IOException e) {

                if(log!=null)log.severe("Could not load plugin-resource!");
                throw e;
            }

            try {
                // Read config
                return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), resource));

            } catch (IOException e) {

                if(i == 0) {
                    if(log!=null)log.severe("Could not load configuration-file!");
                    e.printStackTrace();

                    if(log!=null)log.info("Backing-up config and creating a new one");
                    backupAndDisableConfig(resource);
                }
                else
                    throw e;

            }
        }

        // We should not end up here..
        throw new IOException("Could not create/load configuration-file!");
    }

    public static void backupAndDisableConfig(String resource) throws IOException {

        // Rename the old config to a new-one, so settings are not lost
        Random rand = new Random();
        String newResource = resource + rand.nextInt();

        Files.move(new File(resource), new File(newResource));
    }

    public static File loadAndSaveResource(Plugin plugin, String resource) throws IOException {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        if (!resourceFile.exists()) {
            resourceFile.createNewFile();
            try (InputStream in = plugin.getResourceAsStream(resource);
                 OutputStream out = new FileOutputStream(resourceFile)) {
                ByteStreams.copy(in, out);
            }
        }
        return resourceFile;
    }

    public static boolean validateEntry(String entry, String[] allowed) {
        return validateEntry(entry, allowed, false);
    }
    public static boolean validateEntry(String entry, String[] allowed, boolean caseSensetive) {

        for(String allow : allowed) {

            if(entry.equalsIgnoreCase(allow))
                return true;
        }
        return false;
    }
}
