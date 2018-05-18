package de.dytanic.cloudnet.core.console;

import de.dytanic.cloudnet.console.IConsoleChannel;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.core.event.console.ConsoleChannelMessageReceiveEvent;

public final class MasterConsoleChannel implements IConsoleChannel {

    @Override
    public void onMessage(String message)
    {
        CloudNetDriver.getInstance().getEventManager().callEvent(new ConsoleChannelMessageReceiveEvent(this, message));
    }

    @Override
    public String getName()
    {
        return "master";
    }
}