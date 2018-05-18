package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.Command;
import de.dytanic.cloudnet.driver.CloudNetDriver;

abstract class DefaultCommand extends Command {

    public DefaultCommand()
    {
        prefix = "default";
        description = "default CloudNet command description";
    }

    protected final CloudNetDriver getDriver()
    {
        return CloudNetDriver.getInstance();
    }

}