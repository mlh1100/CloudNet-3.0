package de.dytanic.cloudnet.logging;

import de.dytanic.cloudnet.logging.util.ILevelable;

public interface ILogProvider extends ILogHandlerProvider, ILevelable, AutoCloseable {

    void setLevel(int level);

    ILogProvider log(LogEntry... logEntries);

    boolean hasAsyncSupport();

    /*= ------------------------------------------------------ =*/

    default ILogProvider log(LogEntry logEntry)
    {
        return log(new LogEntry[]{logEntry});
    }

    default void setLevel(LogLevel level)
    {
        setLevel(level.getLevel());
    }

    default ILogProvider log(LogLevel level, String message)
    {
        return log(level, new String[]{message});
    }

    default ILogProvider log(LogLevel level, String... messages)
    {
        return log(level, messages, null);
    }

    default ILogProvider log(LogLevel level, Class<?> clazz, String message)
    {
        return log(level, clazz, new String[]{message});
    }

    default ILogProvider log(LogLevel level, Class<?> clazz, String... messages)
    {
        return log(level, clazz, messages, null);
    }

    default ILogProvider log(LogLevel level, String message, Throwable throwable)
    {
        return log(level, new String[]{message}, throwable);
    }

    default ILogProvider log(LogLevel level, String[] messages, Throwable throwable)
    {
        return log(level, null, messages, throwable);
    }

    default ILogProvider log(LogLevel level, Class<?> clazz, String message, Throwable throwable)
    {
        return log(level, clazz, new String[]{message}, throwable);
    }

    default ILogProvider log(LogLevel level, Class<?> clazz, String[] messages, Throwable throwable)
    {
        return log(new LogEntry(System.currentTimeMillis(), clazz, messages, level, throwable, Thread.currentThread()));
    }

    /*= ------------------------------------------------------ =*/

    default ILogProvider info(String message)
    {
        return log(LogLevel.INFO, message);
    }

    default ILogProvider info(String... messages)
    {
        return log(LogLevel.INFO, messages);
    }

    default ILogProvider info(String message, Class<?> clazz)
    {
        return log(LogLevel.INFO, clazz, message);
    }

    default ILogProvider info(String[] messages, Class<?> clazz)
    {
        return log(LogLevel.INFO, clazz, messages);
    }

    /*= ------------------------------------------------------ =*/

    default ILogProvider warning(String message)
    {
        return log(LogLevel.WARNING, message);
    }

    default ILogProvider warning(String... messages)
    {
        return log(LogLevel.WARNING, messages);
    }

    default ILogProvider warning(String message, Class<?> clazz)
    {
        return log(LogLevel.WARNING, clazz, message);
    }

    default ILogProvider warning(String[] messages, Class<?> clazz)
    {
        return log(LogLevel.WARNING, clazz, messages);
    }

    default ILogProvider warning(String message, Throwable throwable)
    {
        return log(LogLevel.WARNING, message, throwable);
    }

    default ILogProvider warning(String[] messages, Throwable throwable)
    {
        return log(LogLevel.WARNING, messages, throwable);
    }

    default ILogProvider warning(String message, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.WARNING, clazz, message, throwable);
    }

    default ILogProvider warning(String[] messages, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.WARNING, clazz, messages, throwable);
    }

    /*= ------------------------------------------------------ =*/

    default ILogProvider fatal(String message)
    {
        return log(LogLevel.FATAL, message);
    }

    default ILogProvider fatal(String... messages)
    {
        return log(LogLevel.FATAL, messages);
    }

    default ILogProvider fatal(String message, Class<?> clazz)
    {
        return log(LogLevel.FATAL, clazz, message);
    }

    default ILogProvider fatal(String[] messages, Class<?> clazz)
    {
        return log(LogLevel.FATAL, clazz, messages);
    }

    default ILogProvider fatal(String message, Throwable throwable)
    {
        return log(LogLevel.FATAL, message, throwable);
    }

    default ILogProvider fatal(String[] messages, Throwable throwable)
    {
        return log(LogLevel.FATAL, messages, throwable);
    }

    default ILogProvider fatal(String message, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.FATAL, clazz, message, throwable);
    }

    default ILogProvider fatal(String[] messages, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.FATAL, clazz, messages, throwable);
    }

    /*= ------------------------------------------------------ =*/

    default ILogProvider debug(String message)
    {
        return log(LogLevel.DEBUG, message);
    }

    default ILogProvider debug(String... messages)
    {
        return log(LogLevel.DEBUG, messages);
    }

    default ILogProvider debug(String message, Class<?> clazz)
    {
        return log(LogLevel.DEBUG, clazz, message);
    }

    default ILogProvider debug(String[] messages, Class<?> clazz)
    {
        return log(LogLevel.DEBUG, clazz, messages);
    }

    default ILogProvider debug(String message, Throwable throwable)
    {
        return log(LogLevel.DEBUG, message, throwable);
    }

    default ILogProvider debug(String[] messages, Throwable throwable)
    {
        return log(LogLevel.DEBUG, messages, throwable);
    }

    default ILogProvider debug(String message, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.DEBUG, clazz, message, throwable);
    }

    default ILogProvider debug(String[] messages, Class<?> clazz, Throwable throwable)
    {
        return log(LogLevel.DEBUG, clazz, messages, throwable);
    }
}