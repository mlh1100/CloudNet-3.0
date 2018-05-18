package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.permissions.IPermissionProvider;
import de.dytanic.cloudnet.util.Properties;

public final class CommandPermissions extends DefaultCommand {

    public CommandPermissions()
    {
        names = new String[]{"permissions", "perms", "perm"};
        permission = "cloudnet.console.command.permissions";
        description = "manage the permission service of this instance";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {
        IPermissionProvider permissionProvider = CloudNet.getInstance().getPermissionProvider();

        if (args.length == 0)
        {
            sender.sendMessage(
                    
            );

            return;
        }

    }
}