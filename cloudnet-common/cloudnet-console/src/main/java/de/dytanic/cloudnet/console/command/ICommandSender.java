package de.dytanic.cloudnet.console.command;

import de.dytanic.cloudnet.interfaces.INameable;

public interface ICommandSender extends INameable {

    void sendMessage(String message);

    void sendMessage(String... messages);

    boolean hasPermission(String permission);

}