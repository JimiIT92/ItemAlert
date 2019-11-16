package org.itemalert.tasks;

import com.google.gson.JsonArray;
import org.itemalert.settings.PluginSettings;
import org.itemalert.settings.Settings;
import org.itemalert.utils.JsonUtils;
import org.itemalert.utils.LogUtils;
import org.spongepowered.api.scheduler.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Check if Plugin Updates are availables
 *
 * @author JimiIT92
 */
public class UpdateCheckTask implements Consumer<Task> {

    /**
     * Plugin Download URL
     */
    private static final String DOWNLOAD_URL = "https://ore.spongepowered.org/Francesco_Jimi/ItemAlert/versions/";

    /**
     * Check for Updates
     *
     * @param task Task to execute
     */
    @Override
    public void accept(Task task) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(PluginSettings.UPDATE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JsonArray versions = JsonUtils.parse(result.toString(), JsonArray.class);
            float latestVersion = versions.get(0).getAsJsonObject().get("name").getAsFloat();
            if (latestVersion > Settings.PLUGIN_VERSION) {
                LogUtils.getLogger().warn("A new version of ItemAlert is available! (v: " + latestVersion + ")");
                LogUtils.getLogger().warn("You can download it from here: " + DOWNLOAD_URL + latestVersion);
            }
        } catch (IOException ex) {
            LogUtils.getLogger().error(Arrays.toString(ex.getStackTrace()));
        }
    }
}
