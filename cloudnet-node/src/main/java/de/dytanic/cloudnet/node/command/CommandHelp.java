package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.command.Command;
import de.dytanic.cloudnet.console.command.CommandInfo;
import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.console.command.ITabCompletion;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.util.Properties;

import java.util.Arrays;
import java.util.List;

public final class CommandHelp extends DefaultCommand implements ITabCompletion {

    public CommandHelp()
    {
        names = new String[]{"help", "?"};
        description = "default help command for some informations about commands.";
        usage = "help";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {
        switch (args.length)
        {
            case 0:
                execute0(sender);
                break;
            case 1:
                info(sender, args[0]);
                break;
        }
    }

    @Override
    public List<String> complete(String commandLine, String[] args, Properties properties)
    {
        return $.newArrayList(CloudNet.getInstance().getCommandMap().getCommandNames());
    }

    /*= ---------------------------------------------------------------------------------- =*/

    private void info(ICommandSender sender, String command)
    {
        info(sender, CloudNet.getInstance().getCommandMap().getCommand(command));
    }

    private void info(ICommandSender sender, Command command)
    {
        if (command != null)
            sender.sendMessage(
                    $.SPACE_STRING + $.SPACE_STRING,
                    "Usage: \"" + (command.getUsage() != null ? command.getUsage() : "no usage") + "\"",
                    "Aliases: " + Arrays.asList(command.getNames()).toString().replace("[", $.EMPTY_STRING).replace("]", $.EMPTY_STRING),
                    "Permission: " + (command.getPermission() != null ? command.getPermission() : "no permission"),
                    "Description: " + (command.getDescription() != null ? command.getDescription() : "no description"),
                    "JSON Properties: " + command.getProperties()
            );
    }

    private void execute0(ICommandSender sender)
    {
        for (CommandInfo commandInfo : CloudNet.getInstance().getCommandMap().getCommandInfos())
            info(sender, commandInfo.getNames()[0]);
    }

}