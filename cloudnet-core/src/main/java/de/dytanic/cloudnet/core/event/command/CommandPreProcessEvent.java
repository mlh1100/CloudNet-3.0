package de.dytanic.cloudnet.core.event.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import de.dytanic.cloudnet.event.interfaces.Cancelable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public final class CommandPreProcessEvent extends CloudNetCoreEvent implements Cancelable {

    private String commandLine;

    private ICommandSender commandSender;

    @Setter
    private boolean cancelled;

}