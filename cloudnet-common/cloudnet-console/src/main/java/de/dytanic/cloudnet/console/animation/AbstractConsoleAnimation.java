package de.dytanic.cloudnet.console.animation;

import de.dytanic.cloudnet.console.IConsoleProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractConsoleAnimation {

    public abstract void start(IConsoleProvider consoleProvider);

}