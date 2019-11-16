package org.itemalert.commands;

import org.itemalert.model.AlertType;
import org.itemalert.utils.AlertUtils;
import org.itemalert.utils.LogUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Add/Replace an Item Alert for a Player
 *
 * @author JimiIT92
 */
public class ItemAlertAddExecutor implements CommandExecutor {

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
        ItemType item = args.<ItemType>getOne("item").orElse(null);
        int durability = args.<Integer>getOne("durability").orElse(0);
        AlertType alertType = args.<AlertType>getOne("alertType").orElse(AlertType.CHAT);
        if (item != null) {
            AlertUtils.savePlayerAlert(player.getUniqueId(), item, durability, alertType);
        }
        player.sendMessage(Text.of(TextColors.GREEN, "Item Alert added!"));
        player.playSound(SoundTypes.BLOCK_ANVIL_USE, player.getPosition(), 1.0F);
        return CommandResult.success();
    }
}
