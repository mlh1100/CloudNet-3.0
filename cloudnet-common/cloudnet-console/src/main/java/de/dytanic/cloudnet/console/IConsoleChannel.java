package de.dytanic.cloudnet.console;

import de.dytanic.cloudnet.interfaces.INameable;

public interface IConsoleChannel extends INameable {

    void onMessage(String message);

}