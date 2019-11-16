package org.itemalert.model;

/**
 * Alert Model
 *
 * @author JimiIT92
 */
public class Alert {
    /**
     * If the Alert is enabled
     */
    private boolean ENABLED;
    /**
     * Item minimum durability to trigger the Alert
     */
    private int DURABILITY;
    /**
     * Alert Type
     */
    private AlertType TYPE;

    /**
     * Default Constructor. Initialize a new Alert
     * with the specified durability and Alert Type
     *
     * @param durability Minimum durability
     * @param type       Alert Type
     */
    public Alert(int durability, AlertType type) {
        this.ENABLED = true;
        this.DURABILITY = durability;
        this.TYPE = type;
    }

    /**
     * If the Alert is enabled
     *
     * @return If the Alert is enabled
     */
    public boolean isEnabled() {
        return ENABLED;
    }

    /**
     * Enable/Disable the Alert
     *
     * @param enabled If the Alert should be enabled or disabled
     */
    public void setEnabled(boolean enabled) {
        this.ENABLED = enabled;
    }

    /**
     * Get the Item minimum durability to trigger the Alert
     *
     * @return Item minimum durability
     */
    public int getDurability() {
        return DURABILITY;
    }

    /**
     * Get the Alert Type
     *
     * @return Alert Type
     */
    public AlertType getType() {
        return TYPE;
    }
}
