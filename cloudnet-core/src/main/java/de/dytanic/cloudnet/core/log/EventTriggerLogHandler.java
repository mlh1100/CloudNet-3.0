package de.dytanic.cloudnet.core.log;

import de.dytanic.cloudnet.core.event.log.LogEntryReceiveEvent;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.logging.ILogHandler;
import de.dytanic.cloudnet.logging.LogEntry;

public final class EventTriggerLogHandler implements ILogHandler {

    @Override
    public void handle(LogEntry logEntry)
    {
        CloudNetDriver.getInstance().getEventManager().callEvent(new LogEntryReceiveEvent(logEntry));
    }

    @Override
    public void close() throws Exception
    {

    }
}