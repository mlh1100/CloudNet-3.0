package de.dytanic.cloudnet.logging;

public interface ILogHandler extends AutoCloseable {

    void handle(LogEntry logEntry);

}