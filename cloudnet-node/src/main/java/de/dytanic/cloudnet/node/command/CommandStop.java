package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.Command;
import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.console.command.ITabCompletion;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.util.Properties;

import java.util.Arrays;
import java.util.List;

public final class CommandStop extends Command implements ITabCompletion {

    public CommandStop()
    {
        usage = "stop";
        names = new String[]{"stop", "exit", "quit"};
        permission = "cloudnet.console.command.stop";
        description = "stop the instance and all services where running on this instance";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {
        if(CloudNet.getInstance() != null)
        {
            try
            {
                CloudNet.getInstance().close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.exit(0);
        }
    }

    @Override
    public List<String> complete(String commandLine, String[] args, Properties properties)
    {
        return Arrays.asList("Level", "logen", "lervern");
    }
}