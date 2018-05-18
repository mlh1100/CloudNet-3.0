package de.dytanic.cloudnet.logging;

import de.dytanic.cloudnet.logging.util.ILevelable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class LogLevel implements ILevelable {

    public static final LogLevel

            INFO = new LogLevel("information", "INFO", 0, true),
            WARNING = new LogLevel("warning", "WARNING", 1, true),
            ERROR = new LogLevel("error", "ERROR", 2, true),
            FATAL = new LogLevel("fatal", "FATAL", 3, true),
            MODULE = new LogLevel("module", "MODULE", 4, true),
            COMMAND = new LogLevel("command", "COMMAND", 5, false),
            DEBUG = new LogLevel("debug", "DEBUG", Integer.MAX_VALUE, false);

    protected String lowerName, upperName;

    protected int level;

    protected boolean async;

}