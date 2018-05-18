package de.dytanic.cloudnet.node.event;

import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.logging.ILogProvider;

public interface ICloudNetNodeEvent {

    default CloudNet getCloud()
    {
        return CloudNet.getInstance();
    }

    default ILogProvider getLogger()
    {
        return CloudNet.getInstance().getLogProvider();
    }

    default IConsoleProvider getConsole()
    {
        return CloudNet.getInstance().getConsoleProvider();
    }

}