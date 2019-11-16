package org.itemalert.utils;

import org.itemalert.ItemAlert;
import org.slf4j.Logger;

/**
 * Log Utility class
 *
 * @author JimiIT92
 */
public class LogUtils {

    /**
     * Logger Instance
     */
    private static Logger LOGGER;

    /**
     * Initialize the Logger Instance
     *
     * @param plugin Plugin Instance
     */
    public static void init(ItemAlert plugin) {
        LOGGER = plugin.getLogger();
    }

    /**
     * Get the Logger instance
     *
     * @return Logger instance
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}
