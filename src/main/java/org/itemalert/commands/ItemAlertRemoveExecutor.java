package org.itemalert.commands;

import org.itemalert.utils.AlertUtils;
import org.itemalert.utils.LogUtils;
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
 * Remove an Item Alert for a Player
 *
 * @author JimiIT92
 */
public class ItemAlertRemoveExecutor implements CommandExecutor {

    /**
     * Execute the Command
     *
     * @param src  Command Source
     * @param args Command Arguments
     * @return Command Result
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            LogUtils.getLogger().error("This command is only for players!");
            return CommandResult.empty();
        }
        Player player = (Player) src;
        args.<ItemType>getOne("item").ifPresent(item -> {
            if (item.equals(ItemTypes.NONE)) {
                AlertUtils.removeAllPlayerAlerts(player.getUniqueId());
            } else {
                AlertUtils.removePlayerAlert(player.getUniqueId(), item);
            }
        });
        player.sendMessage(Text.of(TextColors.RED, "Item Alert removed!"));
        player.playSound(SoundTypes.ENTITY_ITEM_BREAK, player.getPosition(), 1.0F);
        return CommandResult.success();
    }
}
