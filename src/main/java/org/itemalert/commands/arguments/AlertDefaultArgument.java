package org.itemalert.commands.arguments;

import org.itemalert.model.Alert;
import org.itemalert.model.EnumOperation;
import org.itemalert.utils.AlertUtils;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alert Default Argument
 *
 * @author JimiIT92
 */
public class AlertDefaultArgument extends PatternMatchingCommandElement {

    /**
     * Command Operation
     */
    private EnumOperation operation;

    /**
     * Constructor. Set the Argument name
     *
     * @param key Argument name
     */
    public AlertDefaultArgument(@Nullable Text key) {
        super(key);
    }

    /**
     * Parse the Command Arguments to determine which operation
     * has to be done
     *
     * @param source  Command Source
     * @param args    Command Arguments
     * @param context Command Context
     * @throws ArgumentParseException
     */
    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        this.operation = context.<EnumOperation>getOne("operation").orElse(EnumOperation.ADD);
        super.parse(source, args, context);
    }

    /**
     * Get the available choices for tab completion
     *
     * @param source Command source
     * @return Choices
     */
    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        HashMap<String, Alert> alerts;
        switch (this.operation) {
            case ADD:
            default:
                return Sponge.getRegistry().getAllOf(ItemType.class).stream().map(CatalogType::getId).collect(Collectors.toList());
            case REMOVE:
                alerts = AlertUtils.DEFAULT_ALERTS;
                break;
        }
        if (alerts == null || alerts.size() == 0) {
            return Collections.emptySet();
        }
        List<String> keys = new ArrayList<>();
        keys.add("all");
        alerts.entrySet().stream().filter(alert -> alert.getValue().isEnabled()).forEach(alert -> keys.add(alert.getKey()));
        return keys;
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
