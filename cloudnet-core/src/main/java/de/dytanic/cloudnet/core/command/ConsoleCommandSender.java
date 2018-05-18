package de.dytanic.cloudnet.core.command;

import de.dytanic.cloudnet.console.command.ICommandSender;

public class ConsoleCommandSender implements ICommandSender {

    private final String name = "console";

    @Override
    public void sendMessage(String message)
    {
        System.out.println(message);
    }

    @Override
    public void sendMessage(String... messages)
    {
        for(String message : messages)
            sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return name;
    }
}