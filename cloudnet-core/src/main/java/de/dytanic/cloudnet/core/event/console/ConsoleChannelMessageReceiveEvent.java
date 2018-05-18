package de.dytanic.cloudnet.core.event.console;

import de.dytanic.cloudnet.console.IConsoleChannel;
import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ConsoleChannelMessageReceiveEvent extends CloudNetCoreEvent {

    private IConsoleChannel channel;

    private String message;

}