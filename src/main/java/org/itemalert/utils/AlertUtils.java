package org.itemalert.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.itemalert.ItemAlert;
import org.itemalert.model.Alert;
import org.itemalert.settings.PluginSettings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JimiIT92
 */
public class AlertUtils {

    private static final String DEFAULTS_FILE_NAME = "defaults.json";
    private static final String ALERTS_FOLDER = "alerts";

    public static HashMap<String, Alert> DEFAULT_ALERTS = new HashMap<>();

    public static void init(ItemAlert plugin) {
        loadAlerts(plugin);
    }

    public static void saveAlerts() {
        if (DEFAULT_ALERTS != null) {
            String json = JsonUtils.serializeMap(DEFAULT_ALERTS);
            JsonUtils.saveToFile(json, getConfigFolder(), getDefaultAltersFile());
        }
    }

    private static void loadAlerts(ItemAlert plugin) {
        String json = JsonUtils.loadFromFile(getDefaultAltersFile());
        if (json != null) {
            DEFAULT_ALERTS = new HashMap<>(new Gson().fromJson(json, new TypeToken<Map<String, Alert>>() {
            }.getType()));
        } else {
            SettingsUtils.load(plugin, DEFAULTS_FILE_NAME, false);
            loadAlerts(plugin);
        }
    }

    private static File getDefaultAltersFile() {
        return new File(getConfigFolder(), DEFAULTS_FILE_NAME);
    }

    private static File getConfigFolder() {
        return new File("config/" + PluginSettings.ID.toLowerCase() + "/");
    }

    private static File getAlertsFolder() {
        return new File(getConfigFolder() + "/" + ALERTS_FOLDER + "/");
    }

}
