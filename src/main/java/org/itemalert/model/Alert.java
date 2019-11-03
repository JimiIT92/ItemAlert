package org.itemalert.model;

/**
 * @author JimiIT92
 */
public class Alert {
    private boolean ENABLED;
    private int DURABILITY;
    private AlertType TYPE;

    public Alert(int durability, AlertType type) {
        this(true, durability, type);
    }

    private Alert(boolean enabled, int durability, AlertType type) {
        this.ENABLED = enabled;
        this.DURABILITY = durability;
        this.TYPE = type;
    }

    public boolean isEnabled() {
        return ENABLED;
    }

    public void setEnabled(boolean enabled) {
        this.ENABLED = enabled;
    }

    public int getDurability() {
        return DURABILITY;
    }

    public void setDurability(int durability) {
        this.DURABILITY = durability;
    }

    public AlertType getType() {
        return TYPE;
    }

    public void setType(AlertType type) {
        this.TYPE = type;
    }
}
