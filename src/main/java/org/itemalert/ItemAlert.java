package org.itemalert;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.itemalert.settings.PluginSettings;
import org.itemalert.settings.Settings;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

/**
 * Main Plugin Class
 */
@Plugin(id = PluginSettings.ID,
        name = PluginSettings.NAME,
        description = PluginSettings.DESCRIPTION,
        version = PluginSettings.PLUGIN_VERSION,
        authors = PluginSettings.AUTHORS,
        url = PluginSettings.URL)
public class ItemAlert {

    /**
     * Logger Instance
     */
    @Inject
    private Logger logger;
    /**
     * Default configuration loader
     */
    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    /**
     * Default configuration folder
     */
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;
    /**
     * Game instance
     */
    @Inject
    private Game game;
    /**
     * Plugin Container instance
     */
    @Inject
    private PluginContainer pluginContainer;

    /**
     * Plugin pre-initialization. Loads the settings
     *
     * @param event GamePreInitializationEvent
     */
    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        logger.warn("ItemAlert initializing...");
        Settings.init(this);
    }

    /**
     * Plugin post-initialization. Check for updates
     *
     * @param event GamePostInitializationEvent
     */
    @Listener
    public void onGamePostInitialization(GamePostInitializationEvent event) {
        Settings.checkForUpdates();
        Settings.checkDurabilities();
    }

    /**
     * Initialize Plugin
     *
     * @param event Game Started Server Event
     */
    @Listener
    public void onGameStartedServerEvent(GameStartedServerEvent event) {
        logger.warn("ItemAlert initialized!");
    }


    /**
     * Get the Logger instance
     *
     * @return Logger instance
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get the default configuration folder
     *
     * @return Default configuration folder
     */
    public Path getConfigDirectory() {
        return configDirectory;
    }

    /**
     * Get the Plugin Container instance
     *
     * @return Plugin Container instance
     */
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}
