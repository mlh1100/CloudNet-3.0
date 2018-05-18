package de.dytanic.cloudnet.console.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.ConsoleColour;
import de.dytanic.cloudnet.console.IConsoleChannel;
import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.console.animation.AbstractConsoleAnimation;
import de.dytanic.cloudnet.util.Pair;
import jline.console.ConsoleReader;
import lombok.Getter;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Queue;

public final class JAnsiConsoleProvider implements IConsoleProvider {

    @Getter
    private final ConsoleReader consoleReader;

    @Getter
    private IConsoleChannel consoleChannel;

    private final Queue<Pair<Boolean, String>> animationWaitingMessagesQueue = $.newConcurrentLinkedQueue();

    private volatile boolean animationLock = false;

    private volatile Thread animationThread = null;

    public JAnsiConsoleProvider() throws Exception
    {
        try
        {
            Field field = Charset.class.getDeclaredField("defaultCharset");
            field.setAccessible(true);
            field.set(null, StandardCharsets.UTF_8);
        } catch (Exception ex)
        {
        }

        AnsiConsole.systemInstall();

        this.consoleReader = new ConsoleReader(System.in, System.out);
        this.consoleReader.setExpandEvents(false);

        this.consoleChannel = null;
    }

    @Override
    public void writeLine(String message)
    {
        if (animationLock && animationThread != null && !animationThread.equals(Thread.currentThread()))
        {
            this.animationWaitingMessagesQueue.offer(new Pair<>(true, message));
            return;
        }

        try
        {
            if (!message.endsWith($.LINE_SEPARATOR))
                message = message + $.LINE_SEPARATOR;

            if (this.consoleChannel != null)
                this.consoleChannel.onMessage(message);

            message = ConsoleColour.toColouredString('&', message);
            message = ConsoleColour.toColouredString('§', message);

            this.consoleReader.print(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + message + Ansi.ansi().reset().toString());
            this.consoleReader.drawLine();
            this.consoleReader.flush();

        } catch (IOException e)
        {
        }
    }

    @Override
    public void write(String message)
    {
        if (animationLock && animationThread != null && !animationThread.equals(Thread.currentThread()))
        {
            this.animationWaitingMessagesQueue.offer(new Pair<>(false, message));
            return;
        }

        try
        {
            if (this.consoleChannel != null)
                this.consoleChannel.onMessage(message);

            message = ConsoleColour.toColouredString('&', message);
            message = ConsoleColour.toColouredString('§', message);

            this.consoleReader.print(message);
            this.consoleReader.flush();

        } catch (IOException e)
        {
        }
    }

    @Override
    public final synchronized void invokeConsoleAnimation(AbstractConsoleAnimation consoleAnimation)
    {
        this.animationThread = Thread.currentThread();
        this.animationLock = true;

        try
        {
            consoleAnimation.start(this);
        } finally
        {
            animationThread = null;
            animationLock = false;
            Pair<Boolean, String> message;

            while (!this.animationWaitingMessagesQueue.isEmpty())
            {
                message = this.animationWaitingMessagesQueue.poll();

                if (message.getFirst())
                    writeLine(message.getSecond());
                else
                    write(message.getSecond());
            }

        }
    }

    @Override
    public void setConsoleChannel(IConsoleChannel consoleChannel)
    {
        this.consoleChannel = consoleChannel;
    }

    @Override
    public void clearScreen()
    {
        try
        {
            this.consoleReader.clearScreen();
        } catch (IOException e)
        {
        }
    }

    @Override
    public void close() throws Exception
    {
        this.consoleReader.close();
    }

    /*= -------------------------------------------------------- =*/

}