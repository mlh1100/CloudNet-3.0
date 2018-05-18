package de.dytanic.cloudnet.core.event.console;

import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ConsoleCommandReadPromptFormatEvent extends CloudNetCoreEvent {

    private String prompt;

}