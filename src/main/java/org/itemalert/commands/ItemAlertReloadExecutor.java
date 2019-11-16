package org.itemalert.commands;

import org.itemalert.settings.Settings;
import org.itemalert.utils.AlertUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Reload the Configuration from disk
 *
 * @author JimiIT92
 */
public class ItemAlertReloadExecutor implements CommandExecutor {

    /**
     * Execute the Command
     *
     * @param src  Command Source
     * @param args Command Arguments
     * @return Command Result
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        AlertUtils.init(Settings.getPlugin());
        src.sendMessage(Text.of(TextColors.GREEN, "Item Alert reloaded!"));
        return CommandResult.success();
    }
}
