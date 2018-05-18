package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.util.Properties;

public final class CommandDelete extends DefaultCommand {

    public CommandDelete()
    {
        names = new String[]{"delete", "del", "remove", "rm", "rem"};
        permission = "cloudnet.console.command.delete";
        usage = "del";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {

    }
}