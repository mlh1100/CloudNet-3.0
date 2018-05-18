package de.dytanic.cloudnet.core.event.log;

import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import de.dytanic.cloudnet.logging.LogEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class LogEntryReceiveEvent extends CloudNetCoreEvent {

    private LogEntry logEntry;

}