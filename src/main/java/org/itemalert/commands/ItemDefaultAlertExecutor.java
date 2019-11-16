package org.itemalert.commands;

import org.itemalert.model.AlertType;
import org.itemalert.model.EnumOperation;
import org.itemalert.utils.AlertUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Handle a default Item Alert
 *
 * @author JimiIT92
 */
public class ItemDefaultAlertExecutor implements CommandExecutor {

    /**
     * Execute the Command
     *
     * @param src  Command Source
     * @param args Command Arguments
     * @return Command Result
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = src instanceof Player ? (Player) src : null;
        EnumOperation operation = args.<EnumOperation>getOne("operation").orElse(EnumOperation.ADD);
        ItemType item = args.<ItemType>getOne("item").orElse(null);
        int durability = args.<Integer>getOne("durability").orElse(0);
        AlertType alertType = args.<AlertType>getOne("alertType").orElse(AlertType.CHAT);

        switch (operation) {
            case ADD:
            default:
                AlertUtils.saveDefaultAlert(item, durability, alertType);
                src.sendMessage(Text.of(TextColors.GREEN, "Item Alert added!"));
                if (player != null) {
                    player.playSound(SoundTypes.BLOCK_ANVIL_USE, player.getPosition(), 1.0F);
                }
                break;
            case REMOVE:
                assert item != null;
                if (item.equals(ItemTypes.NONE)) {
                    AlertUtils.removeAllDefaultAlerts();
                } else {
                    AlertUtils.removeDefaultAlert(item);
                }
                src.sendMessage(Text.of(TextColors.RED, "Item Alert removed!"));
                if (player != null) {
                    player.playSound(SoundTypes.ENTITY_ITEM_BREAK, player.getPosition(), 1.0F);
                }
                break;
        }
        return CommandResult.success();
    }
}
