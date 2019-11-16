package org.itemalert;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.itemalert.commands.*;
import org.itemalert.commands.arguments.AlertDefaultArgument;
import org.itemalert.commands.arguments.AlertItemArgument;
import org.itemalert.model.AlertType;
import org.itemalert.model.EnumOperation;
import org.itemalert.settings.PluginSettings;
import org.itemalert.settings.Settings;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

/**
 * Item Alert Main Class
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
     * Constructor
     *
     * @param logger          Logger Instance
     * @param loader          Configuration Loader Instance
     * @param configDirectory Configuration Directory
     * @param game            Game Instance
     * @param pluginContainer Plugin Container Instance
     */
    public ItemAlert(Logger logger, ConfigurationLoader<CommentedConfigurationNode> loader, Path configDirectory, Game game, PluginContainer pluginContainer) {
        this.logger = logger;
        this.loader = loader;
        this.configDirectory = configDirectory;
        this.game = game;
        this.pluginContainer = pluginContainer;
    }

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
     * Plugin initialization. Register the commands
     *
     * @param event GameInitializationEvent
     */
    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        CommandSpec itemAlertHelpSpec = CommandSpec.builder()
                .executor(new ItemAlertHelpExecutor())
                .build();

        CommandSpec itemAlertAddSpec = CommandSpec.builder()
                .arguments(GenericArguments.catalogedElement(Text.of("item"), ItemType.class),
                        GenericArguments.integer(Text.of("durability")),
                        GenericArguments.optional(GenericArguments.enumValue(Text.of("alertType"), AlertType.class)))
                .executor(new ItemAlertAddExecutor())
                .build();

        CommandSpec itemAlertRemoveSpec = CommandSpec.builder()
                .executor(new ItemAlertRemoveExecutor())
                .arguments(new AlertItemArgument(Text.of("item")))
                .build();

        CommandSpec itemDefaultSpec = CommandSpec.builder()
                .executor(new ItemDefaultAlertExecutor())
                .permission(Settings.ITEM_ALERT_PERMISSION)
                .arguments(GenericArguments.enumValue(Text.of("operation"), EnumOperation.class),
                        new AlertDefaultArgument(Text.of("item")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("durability"))),
                        GenericArguments.optional(GenericArguments.enumValue(Text.of("alertType"), AlertType.class)))
                .build();

        CommandSpec itemReloadSpec = CommandSpec.builder()
                .executor(new ItemAlertReloadExecutor())
                .permission(Settings.ITEM_ALERT_PERMISSION)
                .build();

        CommandSpec.Builder itemAlertSpec = CommandSpec.builder()
                .child(itemAlertHelpSpec, "help")
                .child(itemReloadSpec, "reload")
                .child(itemDefaultSpec, "default");

        if (Settings.ALLOW_PLAYERS_OVERRIDE) {
            itemAlertSpec.child(itemAlertAddSpec, "add").child(itemAlertRemoveSpec, "remove");
        }

        Sponge.getCommandManager().register(this, itemAlertSpec.build(), Lists.newArrayList("itemAlert", "ia"));
    }

    /**
     * Plugin post-initialization. Check for updates
     *
     * @param event GamePostInitializationEvent
     */
    @Listener
    public void onGamePostInitialization(GamePostInitializationEvent event) {
        Settings.checkForUpdates();
        Settings.checkDurability();
    }

    /**
     * Server started. Logs that the plugin has been initialized
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
