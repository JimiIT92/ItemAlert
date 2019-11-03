package org.itemalert.utils;

import org.itemalert.ItemAlert;
import org.slf4j.Logger;

/**
 * @author JimiIT92
 */
public class LogUtils {

    private static Logger LOGGER;

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
