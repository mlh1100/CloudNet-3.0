package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.util.Properties;

public final class CommandCreate extends DefaultCommand {

    public CommandCreate()
    {
        names = new String[]{"create"};
        permission = "cloudnet.console.command.create";
        description = "creates a new job or service instance and manage all param configurations";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {

    }
}