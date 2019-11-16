package org.itemalert.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.itemalert.ItemAlert;
import org.itemalert.model.Alert;
import org.itemalert.model.AlertType;
import org.itemalert.settings.PluginSettings;
import org.itemalert.settings.Settings;
import org.spongepowered.api.item.ItemType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Alert Utility class
 *
 * @author JimiIT92
 */
public class AlertUtils {

    /**
     * Defaults file name
     */
    private static final String DEFAULTS_FILE_NAME = "defaults.json";
    /**
     * Alerts Folder name
     */
    private static final String ALERTS_FOLDER = "alerts";
    /**
     * Default Item Alerts
     */
    public static HashMap<String, Alert> DEFAULT_ALERTS = new HashMap<>();
    /**
     * Players Item Alerts
     */
    public static HashMap<UUID, HashMap<String, Alert>> PLAYER_ALERTS = new HashMap<>();

    /**
     * Initialize the Alerts
     *
     * @param plugin Plugin Instance
     */
    public static void init(ItemAlert plugin) {
        loadDefaultAlerts(plugin);
        if (Settings.ALLOW_PLAYERS_OVERRIDE) {
            loadPlayerAlerts();
        }
    }

    /**
     * Save the Default Alerts to disk
     */
    private static void saveDefaultAlerts() {
        if (DEFAULT_ALERTS != null) {
            String json = JsonUtils.serializeMap(DEFAULT_ALERTS);
            JsonUtils.saveToFile(json, getConfigFolder(), getDefaultAlertsFile());
        }
    }

    /**
     * Load the Default Alerts from the disk
     *
     * @param plugin Plugin Instance
     */
    private static void loadDefaultAlerts(ItemAlert plugin) {
        String json = JsonUtils.loadFromFile(getDefaultAlertsFile());
        if (json != null) {
            DEFAULT_ALERTS = new HashMap<>(new Gson().fromJson(json, new TypeToken<Map<String, Alert>>() {
            }.getType()));
        } else {
            SettingsUtils.load(plugin, DEFAULTS_FILE_NAME, false);
            loadDefaultAlerts(plugin);
        }
    }

    /**
     * Load the Player Alerts
     */
    private static void loadPlayerAlerts() {
        try {
            Files.list(Paths.get(getAlertsFolder().toURI())).forEach(alert -> {
                String json = JsonUtils.loadFromFile(alert.toFile());
                if (json != null) {
                    PLAYER_ALERTS.put(UUID.fromString(alert.getFileName().toString().substring(0, alert.getFileName().toString().lastIndexOf('.'))), new HashMap<>(new Gson().fromJson(json, new TypeToken<Map<String, Alert>>() {
                    }.getType())));
                }
            });
        } catch (IOException e) {
            LogUtils.getLogger().error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    /**
     * Save/Replace a default Alert
     *
     * @param item       Alert Item
     * @param durability Alert minimum durability
     * @param alertType  Alert Type
     */
    public static void saveDefaultAlert(ItemType item, int durability, AlertType alertType) {
        if (DEFAULT_ALERTS == null) {
            DEFAULT_ALERTS = new HashMap<>();
        }
        DEFAULT_ALERTS.put(item.getId(), new Alert(durability, alertType));
        saveDefaultAlerts();
    }

    /**
     * Remove a default Alert
     *
     * @param item Alert Item
     */
    public static void removeDefaultAlert(ItemType item) {
        if (DEFAULT_ALERTS != null) {
            DEFAULT_ALERTS.remove(item.getId());
            saveDefaultAlerts();
        }
    }

    /**
     * Save/Replace a Player Alert
     *
     * @param player     Player UUID
     * @param item       Alert Item
     * @param durability Alert minimum durability
     * @param alertType  Alert Type
     */
    public static void savePlayerAlert(UUID player, ItemType item, int durability, AlertType alertType) {
        HashMap<String, Alert> playerAlerts = PLAYER_ALERTS.get(player);
        if (playerAlerts == null) {
            playerAlerts = new HashMap<>();
        }
        playerAlerts.put(item.getId(), new Alert(durability, alertType));
        PLAYER_ALERTS.put(player, playerAlerts);
        String json = JsonUtils.serializeMap(playerAlerts);
        JsonUtils.saveToFile(json, getAlertsFolder(), getAlertFile(player));
    }

    /**
     * Remove a Player Alert
     *
     * @param player Player UUID
     * @param item   Alert Item
     */
    public static void removePlayerAlert(UUID player, ItemType item) {
        HashMap<String, Alert> playerAlerts = PLAYER_ALERTS.get(player);
        if (playerAlerts != null) {
            Alert alert = playerAlerts.get(item.getId());
            alert.setEnabled(false);
            playerAlerts.put(item.getId(), alert);
            PLAYER_ALERTS.put(player, playerAlerts);
            String json = JsonUtils.serializeMap(playerAlerts);
            JsonUtils.saveToFile(json, getAlertsFolder(), getAlertFile(player));
        }
    }

    /**
     * Remove all default Alerts
     */
    public static void removeAllDefaultAlerts() {
        if (DEFAULT_ALERTS != null) {
            DEFAULT_ALERTS.clear();
            saveDefaultAlerts();
        }
    }

    /**
     * Remove all Player Alerts
     *
     * @param player Player UUID
     */
    public static void removeAllPlayerAlerts(UUID player) {
        HashMap<String, Alert> playerAlerts = PLAYER_ALERTS.get(player);
        if (playerAlerts != null) {
            playerAlerts.forEach((key, value) -> value.setEnabled(false));
            PLAYER_ALERTS.put(player, playerAlerts);
            String json = JsonUtils.serializeMap(playerAlerts);
            JsonUtils.saveToFile(json, getAlertsFolder(), getAlertFile(player));
        }
    }

    /**
     * Get an Alert for an Item, if present
     *
     * @param player Player UUID
     * @param item   Alert Item
     * @return Item Alert if present, null otherwise
     */
    public static Alert getAlertForItem(UUID player, ItemType item) {
        HashMap<String, Alert> playerAlerts = PLAYER_ALERTS.get(player);
        if (playerAlerts == null) {
            return null;
        }
        return playerAlerts.get(item.getId());
    }

    /**
     * Get the default Alerts file
     *
     * @return Default Alerts file
     */
    private static File getDefaultAlertsFile() {
        return new File(getConfigFolder(), DEFAULTS_FILE_NAME);
    }

    /**
     * Get the configuration folder
     *
     * @return Configuration folder
     */
    private static File getConfigFolder() {
        return new File("config/" + PluginSettings.ID.toLowerCase() + "/");
    }

    /**
     * Get the Alerts folder
     *
     * @return Alerts folder
     */
    private static File getAlertsFolder() {
        return new File(getConfigFolder() + "/" + ALERTS_FOLDER + "/");
    }

    /**
     * Get the Alert file for a Player
     *
     * @param player Player UUID
     * @return Player Alert file
     */
    private static File getAlertFile(UUID player) {
        return new File(getAlertsFolder() + "/" + player.toString() + ".json");
    }
}
