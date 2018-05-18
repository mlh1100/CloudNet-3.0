package de.dytanic.cloudnet.bridge.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "cloudnet_bridge",
        name = "CloudNet-Bridge",
        version = "3.0.0",
        description = "A Sponge implementation for the CloudNet-Bridge",
        url = "http://dytanic.de",
        authors = "Dytanic"
)
public final class SpongeCloudNetBridgePlugin {

    @Listener(order = Order.FIRST)
    public void start(GameStartedServerEvent event)
    {

    }

    @Listener(order = Order.FIRST)
    public void stop(GameStoppedServerEvent event)
    {

    }

}