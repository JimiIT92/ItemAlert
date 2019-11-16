package org.itemalert.model;

/**
 * Alert Types
 *
 * @author JimiIT92
 */
public enum AlertType {
    /**
     * Only a Sound Effect will be played when the Alert triggers
     */
    SOUND,
    /**
     * A message in the Hotbar will appear when the Alert triggers
     */
    HOTBAR,
    /**
     * A message in the Chat will appear when the Alert triggers
     */
    CHAT,
    /**
     * A message in the Hotbar will appear and
     * a Sound Effect will be played when the Alert triggers
     */

    SOUND_HOTBAR,
    /**
     * A message in the Chat will appear and
     * a Sound Effect will be played when the Alert triggers
     */
    SOUND_CHAT
}
