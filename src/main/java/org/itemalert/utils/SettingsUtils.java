package org.itemalert.utils;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.itemalert.ItemAlert;
import org.itemalert.settings.PluginSettings;
import org.spongepowered.api.asset.Asset;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author JimiIT92
 */
public class SettingsUtils {

    public static ConfigurationNode load(ItemAlert plugin, String fileName) {
        return load(plugin, fileName, true);
    }

    static ConfigurationNode load(ItemAlert plugin, String fileName, boolean includeVersion) {
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        String configFileName = fileName;
        if (ext.isEmpty() || ext.equals(fileName)) {
            ext = "conf";
            configFileName = fileName + "." + ext;
        }
        try {
            File configFile = new File(plugin.getConfigDirectory().toFile(), configFileName);
            GsonConfigurationLoader configLoader = GsonConfigurationLoader.builder().setFile(configFile).build();
            Optional<Asset> asset = plugin.getPluginContainer().getAsset(configFileName);
            if (asset.isPresent()) {
                if (!configFile.exists()) {
                    String[] paths = configFileName.split("/");
                    if (paths.length > 1) {
                        for (int i = 0; i < paths.length - 1; i++) {
                            File directory = new File(plugin.getConfigDirectory().toFile(), paths[i]);
                            if (!directory.exists()) {
                                directory.mkdir();
                            }
                        }
                    }
                    asset.get().copyToFile(configFile.toPath());
                } else {
                    ConfigurationNode newNode = GsonConfigurationLoader.builder().setURL(asset.get().getUrl()).build().load();
                    ConfigurationNode oldNode = configLoader.load();
                    oldNode.mergeValuesFrom(newNode);
                    if (includeVersion) {
                        oldNode.getNode("_version").setValue(PluginSettings.VERSION);
                    }
                    configLoader.save(oldNode);
                }
                return configLoader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
            }
        } catch (IOException e) {
            plugin.getLogger().warn("An error occurred while loading settings file: " + fileName);
            plugin.getLogger().error(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
