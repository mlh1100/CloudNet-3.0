package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.util.Properties;

public final class CommandModules extends DefaultCommand {

    public CommandModules()
    {
        names = new String[]{"modules", "module"};
        permission = "cloudnet.console.command.modules";
        description = "manage the module service of this instance";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {

        switch (args.length)
        {
            case 1:
            {

            }
            break;
        }
    }

}