package de.dytanic.cloudnet.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogEntry {

    private long timeStamp;

    private Class<?> clazz;

    private String[] messages;

    private LogLevel logLevel;

    private Throwable throwable;

    private Thread thread;

}