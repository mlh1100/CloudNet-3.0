package de.dytanic.cloudnet.core.event;

import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.core.AbstractCloudNetApplication;
import de.dytanic.cloudnet.logging.ILogProvider;

interface ICloudNetCoreEvent {

    default ILogProvider getLogger()
    {
        return getDriver().getLogProvider();
    }

    default IConsoleProvider getConsole()
    {
        return getDriver().getConsoleProvider();
    }

    default AbstractCloudNetApplication getDriver()
    {
        return (AbstractCloudNetApplication) AbstractCloudNetApplication.getInstance();
    }

}