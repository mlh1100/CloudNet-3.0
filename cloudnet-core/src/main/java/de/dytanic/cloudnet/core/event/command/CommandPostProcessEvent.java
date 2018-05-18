package de.dytanic.cloudnet.core.event.command;

import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.core.event.CloudNetCoreEventAsync;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;
import lombok.Getter;

@Getter
public final class CommandPostProcessEvent extends CloudNetCoreEventAsync<CommandPostProcessEvent> {

    private ICommandSender commandSender;

    private String commandLine;

    public CommandPostProcessEvent(ICommandSender sender, String commandLine)
    {
        super(new Callback<CommandPostProcessEvent>() {
            @Override
            public void call(CommandPostProcessEvent commandPostProcessEvent)
            {

            }
        });

        this.commandLine = commandLine;
        this.commandSender = sender;
    }
}