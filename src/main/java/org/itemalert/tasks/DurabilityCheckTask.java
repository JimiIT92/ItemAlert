package org.itemalert.tasks;

import org.itemalert.model.Alert;
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
import java.util.function.Consumer;

/**
 * @author JimiIT92
 */
public class DurabilityCheckTask implements Consumer<Task> {

    @Override
    public void accept(Task task) {
        Sponge.getServer().getOnlinePlayers().stream().filter(player -> player.gameMode().get().equals(GameModes.SURVIVAL)).forEach(player -> {
            ItemStack mainHandItemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
            ItemStack offHandItemStack = player.getItemInHand(HandTypes.OFF_HAND).orElse(null);
            if (checkDurability(mainHandItemStack)) {
                sendAlert(player, mainHandItemStack);
            }
            if (checkDurability(offHandItemStack)) {
                sendAlert(player, offHandItemStack);
            }
        });
    }

    private boolean checkDurability(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Alert alert = AlertUtils.DEFAULT_ALERTS.get(stack.getType().getId());
        if (alert == null) {
            return false;
        }
        return stack.getValue(Keys.ITEM_DURABILITY).filter(integerMutableBoundedValue -> integerMutableBoundedValue.get() <= alert.getDurability()).isPresent();
    }

    private void sendAlert(Player player, ItemStack stack) {
        Alert alert = AlertUtils.DEFAULT_ALERTS.get(stack.getType().getId());
        String itemName = stack.getTranslation().get();
        int durability = stack.getValue(Keys.ITEM_DURABILITY).get().get();
        Optional<Value<Text>> displayName = stack.getValue(Keys.DISPLAY_NAME);
        if (displayName.isPresent()) {
            itemName = displayName.get().get().toPlainSingle();
        }
        Text text = Text.of(TextColors.RED, "Your ", itemName, " is breaking! [" + durability + " uses left]");
        player.playSound(SoundTypes.ENTITY_ITEM_BREAK, player.getPosition(), 1.0F);
        switch (alert.getType()) {
            case SOUND:
                break;
            case HOTBAR:
                player.sendMessage(ChatTypes.ACTION_BAR, text);
                break;
            case CHAT:
            default:
                player.sendMessage(text);
                break;
        }
    }
}
