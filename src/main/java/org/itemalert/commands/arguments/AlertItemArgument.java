package org.itemalert.commands.arguments;

import org.itemalert.model.Alert;
import org.itemalert.utils.AlertUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Alert Item Argument
 *
 * @author JimiIT92
 */
public class AlertItemArgument extends PatternMatchingCommandElement {

    /**
     * Constructor. Set the Argument name
     *
     * @param key Argument name
     */
    public AlertItemArgument(@Nullable Text key) {
        super(key);
    }

    /**
     * Get the available choices for tab completion
     *
     * @param source Command source
     * @return Choices
     */
    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        HashMap<String, Alert> alerts = AlertUtils.PLAYER_ALERTS.get(((Player) source).getUniqueId());
        if (alerts != null && alerts.size() > 0) {
            List<String> keys = new ArrayList<>();
            keys.add("all");
            alerts.entrySet().stream().filter(alert -> alert.getValue().isEnabled()).forEach(alert -> keys.add(alert.getKey()));
            return keys;
        }
        return Collections.emptySet();
    }

    /**
     * Get an Item Type based on the choice
     *
     * @param choice Selected choice
     * @return Item Type from the choice
     * @throws IllegalArgumentException
     */
    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        if (!choice.isEmpty()) {
            if (choice.equalsIgnoreCase("all")) {
                return ItemTypes.NONE;
            }
            return Sponge.getRegistry().getType(ItemType.class, choice).orElse(null);
        }
        return null;
    }
}
