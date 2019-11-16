package org.itemalert.tasks;

import org.itemalert.model.Alert;
import org.itemalert.settings.Settings;
import org.itemalert.utils.AlertUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Check durability periodically for all online players
 *
 * @author JimiIT92
 */
public class DurabilityCheckTask implements Consumer<Task> {

    /**
     * Check durability for all online players
     *
     * @param task Task to execute
     */
    @Override
    public void accept(Task task) {
        Sponge.getServer().getOnlinePlayers().stream().filter(player -> player.gameMode().get().equals(GameModes.SURVIVAL)).forEach(this::accept);
    }

    /**
     * Check if an Alert should be fired for an item
     *
     * @param player Player
     * @param stack  ItemStack to check
     * @return True if an Alert should be fired, false otherwise
     */
    private boolean checkDurability(UUID player, ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Alert alert = Settings.ALLOW_PLAYERS_OVERRIDE ? AlertUtils.getAlertForItem(player, stack.getType()) : null;
        if (alert != null) {
            if (!alert.isEnabled()) {
                return false;
            }
        } else {
            alert = AlertUtils.DEFAULT_ALERTS.get(stack.getType().getId());
        }
        if (alert == null) {
            return false;
        }
        Alert finalAlert = alert;
        return stack.getValue(Keys.ITEM_DURABILITY).filter(integerMutableBoundedValue -> integerMutableBoundedValue.get() <= finalAlert.getDurability()).isPresent();
    }

    /**
     * Send an Alert to the player
     *
     * @param player Player
     * @param stack  ItemStack for the Alert
     */
    private void sendAlert(Player player, ItemStack stack) {
        Alert alert = Settings.ALLOW_PLAYERS_OVERRIDE ? AlertUtils.getAlertForItem(player.getUniqueId(), stack.getType()) : AlertUtils.DEFAULT_ALERTS.get(stack.getType().getId());
        String itemName = stack.getTranslation().get();
        int durability = stack.getValue(Keys.ITEM_DURABILITY).get().get();
        Optional<Value<Text>> displayName = stack.getValue(Keys.DISPLAY_NAME);
        if (displayName.isPresent()) {
            itemName = displayName.get().get().toPlainSingle();
        }
        Text text = Text.of(TextColors.RED, "Your ", itemName, " is breaking! [" + durability + " uses left]");
        player.playSound(SoundTypes.ENTITY_ITEM_BREAK, player.getPosition(), 1.0F);
        assert alert != null;
        switch (alert.getType()) {
            case SOUND:
                break;
            case HOTBAR:
            case SOUND_HOTBAR:
                player.sendMessage(ChatTypes.ACTION_BAR, text);
                break;
            case CHAT:
            case SOUND_CHAT:
            default:
                player.sendMessage(text);
                break;
        }
    }

    /**
     * Run the durability checks for a Player
     *
     * @param player Player
     */
    private void accept(Player player) {
        ItemStack[] stacks = {
                player.getItemInHand(HandTypes.MAIN_HAND).orElse(null),
                player.getItemInHand(HandTypes.OFF_HAND).orElse(null),
                player.getHelmet().orElse(null),
                player.getChestplate().orElse(null),
                player.getLeggings().orElse(null),
                player.getBoots().orElse(null)
        };

        for (ItemStack stack : stacks) {
            if (checkDurability(player.getUniqueId(), stack)) {
                sendAlert(player, stack);
            }
        }
    }
}
