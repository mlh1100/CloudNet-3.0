package de.dytanic.cloudnet.core;

import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.console.command.ICommandMap;
import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.core.command.ConsoleCommandSender;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.util.Properties;
import lombok.Getter;

@Getter
public abstract class AbstractCloudNetApplication extends CloudNetDriver {

    public static volatile boolean RUNNING = true;

    @Getter
    private final ICommandSender consoleCommandSender = new ConsoleCommandSender();

    protected String[] arguments;

    protected ILogProvider logProvider;

    protected IConsoleProvider consoleProvider;

    protected Properties properties;

    protected ICommandMap commandMap;

    public abstract void setup() throws Exception;

    public abstract void start() throws Exception;

    public abstract void reload() throws Exception;
}