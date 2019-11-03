package org.itemalert;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "itemalert",
        name = "ItemAlert",
        description = "Get an alert sound when a Tool is about to break!",
        authors = {
                "JimiIT92"
        }
)
public class ItemAlert {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
