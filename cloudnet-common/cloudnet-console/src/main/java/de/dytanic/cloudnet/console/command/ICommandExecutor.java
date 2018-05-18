package de.dytanic.cloudnet.console.command;

import de.dytanic.cloudnet.util.Properties;

public interface ICommandExecutor {

    void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties);

}