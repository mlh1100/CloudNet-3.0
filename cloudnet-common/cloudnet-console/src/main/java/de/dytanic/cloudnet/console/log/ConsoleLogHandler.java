package de.dytanic.cloudnet.console.log;

import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.logging.ILogHandler;
import de.dytanic.cloudnet.logging.LogEntry;
import de.dytanic.cloudnet.logging.util.AbstractLogHandler;
import de.dytanic.cloudnet.logging.util.IFormatter;
import lombok.Getter;
import lombok.Setter;

public final class ConsoleLogHandler extends AbstractLogHandler {

    private IConsoleProvider consoleProvider;

    public ConsoleLogHandler(IConsoleProvider consoleProvider)
    {
        this.consoleProvider = consoleProvider;
    }

    @Override
    public void handle(LogEntry logEntry)
    {
        this.consoleProvider.writeLine(formatter.format(logEntry));
    }

    @Override
    public void close() throws Exception
    {

    }
}