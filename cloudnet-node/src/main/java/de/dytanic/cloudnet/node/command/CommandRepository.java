package de.dytanic.cloudnet.node.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.util.Properties;

public final class CommandRepository extends DefaultCommand {

    public CommandRepository()
    {
        names = new String[]{"repository", "repo", "repos", "repositories"};
        permission = "cloudnet.console.command.repository";
        description = "The repository management command";
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
    {

    }
}