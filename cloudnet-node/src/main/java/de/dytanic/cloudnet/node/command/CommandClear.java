package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.util.Properties;

import java.io.IOException;

public final class CommandClear extends DefaultCommand {

    public CommandClear()
    {
        usage = "clearScreen";
        names = new String[]{"clearScreen", "clear", "cls", "cc", "cs"};
        permission = "cloudnet.console.command.clearScreen";
        description = "clears the console.";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {
        try
        {
            CloudNet.getInstance().getConsoleProvider().getConsoleReader().clearScreen();
        } catch (IOException e)
        {
        }
    }
}