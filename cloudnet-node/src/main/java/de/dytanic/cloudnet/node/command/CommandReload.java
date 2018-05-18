package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.util.Properties;

public final class CommandReload extends DefaultCommand {

    public CommandReload()
    {
        names = new String[]{"reload", "rl"};
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {

    }
}