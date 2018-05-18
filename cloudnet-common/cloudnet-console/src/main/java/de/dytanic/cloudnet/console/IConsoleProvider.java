package de.dytanic.cloudnet.console;

import de.dytanic.cloudnet.console.animation.AbstractConsoleAnimation;
import jline.console.ConsoleReader;

public interface IConsoleProvider extends AutoCloseable {

    void writeLine(String message);

    void write(String message);

    void invokeConsoleAnimation(AbstractConsoleAnimation consoleAnimation);

    ConsoleReader getConsoleReader();

    void setConsoleChannel(IConsoleChannel consoleChannel);

    IConsoleChannel getConsoleChannel();

    void clearScreen();

    /*= -------------------------------------------- =*/

    default void writeLine()
    {
        writeLine("");
    }

    default void writeLine(Object object)
    {
        writeLine(object != null ? object.toString() : "");
    }

    default void write()
    {
        write("");
    }

    default void write(Object object)
    {
        write(object != null ? object.toString() : "");
    }

    default String getConsoleChannelName()
    {
        return getConsoleChannel() != null ? getConsoleChannel().getName() : "noChannel";
    }

}