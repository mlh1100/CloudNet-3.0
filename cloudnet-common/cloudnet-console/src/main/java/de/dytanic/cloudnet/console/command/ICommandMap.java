package de.dytanic.cloudnet.console.command;
import java.util.Collection;

public interface ICommandMap {

    void registerCommand(Command command);

    void unregisterCommand(String command);

    void unregisterCommand(Class<? extends Command> command);

    void unregisterCommands();

    Collection<CommandInfo> getCommandInfos();

    Collection<String> getCommandNames();

    Command getCommand(String name);

    Command getCommandFromLine(String commandLine);

    boolean dispatchCommand(ICommandSender commandSender, String commandLine);

    /*= ----------------------------------------- =*/

    default void registerCommand(Command... commands)
    {
        if (commands != null)
            for (Command command : commands)
                if (command != null)
                    registerCommand(command);
    }

    default void unregisterCommand(String... commands)
    {
        if (commands != null)
            for (String command : commands)
                if (command != null)
                    unregisterCommand(command);
    }

    default void unregisterCommand(Class<? extends Command>... commands)
    {
        if (commands != null)
            for (Class<? extends Command> c : commands)
                if (c != null)
                    unregisterCommand(c);
    }

}