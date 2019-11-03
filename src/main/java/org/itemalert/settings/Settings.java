package org.itemalert.settings;

import javafx.util.Pair;
import ninja.leaping.configurate.ConfigurationNode;
import org.itemalert.ItemAlert;
import org.itemalert.tasks.DurabilityCheckTask;
import org.itemalert.tasks.UpdateCheckTask;
import org.itemalert.utils.AlertUtils;
import org.itemalert.utils.LogUtils;
import org.itemalert.utils.SettingsUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author JimiIT92
 */
public class Settings {

    /**
     * Plugin Version
     */
    public static float PLUGIN_VERSION;
    /**
     * Task Builder
     */
    private static Task.Builder TASK_BUILDER;
    /**
     * Plugin Instance
     */
    private static ItemAlert PLUGIN;
    /**
     * How often the plugin should check for updates
     */
    private static Pair<Integer, TimeUnit> UPDATE_CHECK_FREQUENCY;
    /**
     * Set if the plugin should check periodically for updates
     */
    private static boolean CHECK_UPDATES;
    /**
     * How often the alert should be fired
     */
    private static Pair<Integer, TimeUnit> ALERT_FREQUENCY;

    public static void init(ItemAlert plugin) {
        PLUGIN = plugin;
        LogUtils.init(plugin);
        TASK_BUILDER = Sponge.getScheduler().createTaskBuilder();
        loadSettings();
        AlertUtils.init(plugin);
    }

    /**
     * Load the Settings
     */
    private static void loadSettings() {
        ConfigurationNode MAIN_NODE = SettingsUtils.load(PLUGIN, PluginSettings.ID);
        assert MAIN_NODE != null;
        PLUGIN_VERSION = MAIN_NODE.getNode("_version").getFloat();
        ConfigurationNode updateNode = MAIN_NODE.getNode("update_check");
        CHECK_UPDATES = updateNode.getNode("check").getBoolean();
        UPDATE_CHECK_FREQUENCY = new Pair<>(updateNode.getNode("time").getInt(), TimeUnit.valueOf(Objects.requireNonNull(updateNode.getNode("unit").getString()).toUpperCase()));
        ConfigurationNode alertNode = MAIN_NODE.getNode("alert_frequency");
        ALERT_FREQUENCY = new Pair<>(alertNode.getNode("time").getInt(), TimeUnit.valueOf(Objects.requireNonNull(alertNode.getNode("unit").getString()).toUpperCase()));
    }

    /**
     * Check for Updates
     */
    public static void checkForUpdates() {
        if (CHECK_UPDATES) {
            TASK_BUILDER.execute(new UpdateCheckTask()).async().delay(30, TimeUnit.SECONDS).interval(UPDATE_CHECK_FREQUENCY.getKey(), UPDATE_CHECK_FREQUENCY.getValue()).submit(PLUGIN);
        }
    }

    public static void checkDurabilities() {
        TASK_BUILDER.execute(new DurabilityCheckTask()).async().delay(1, TimeUnit.SECONDS).interval(ALERT_FREQUENCY.getKey(), ALERT_FREQUENCY.getValue()).submit(PLUGIN);
    }
}
