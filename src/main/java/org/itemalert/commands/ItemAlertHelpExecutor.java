package org.itemalert.commands;

import org.itemalert.settings.Settings;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Shows the Help
 *
 * @author JimiIT92
 */
public class ItemAlertHelpExecutor implements CommandExecutor {

    /**
     * Execute the Command
     *
     * @param src  Command Source
     * @param args Command Arguments
     * @return Command Result
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!Settings.ALLOW_PLAYERS_OVERRIDE && !src.hasPermission(Settings.ITEM_ALERT_PERMISSION)) {
            src.sendMessage(Text.of(TextColors.RED, "You do not have permission to use this command"));
            return CommandResult.empty();
        }
        sendText(src, "~_____________________ITEM ALERT HELP_____________________________~");
        sendText(src, "| Here is the available sub-commands:");
        sendText(src, "| - add <item> <durability> [alertType]: Add/replace an alert for an item.");
        sendText(src, "| - remove <item>: Remove/disable an alert for an item.");
        if (src.hasPermission(Settings.ITEM_ALERT_PERMISSION)) {
            sendText(src, "| - reload: Reload configuration from disk.");
            sendText(src, "| - default <operation> <args>: Manage default alerts.");
            sendText(src, "| The args will change based on the operation (see add/remove sub-commands)");
        }
        sendText(src, "| - help: Shows this list.");
        sendText(src, "|________________________________________________________________|");
        return CommandResult.success();
    }

    /**
     * Send a gold message
     *
     * @param src  Command Source
     * @param text Message to send
     */
    private void sendText(CommandSource src, String text) {
        src.sendMessage(Text.of(TextColors.GOLD, text));
    }
}
