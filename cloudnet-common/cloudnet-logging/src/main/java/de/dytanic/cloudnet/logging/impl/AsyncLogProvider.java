package de.dytanic.cloudnet.logging.impl;

import de.dytanic.cloudnet.logging.ILogHandler;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.logging.LogEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncLogProvider implements ILogProvider {

    protected final Collection<ILogHandler> handlers = new HashSet<>();

    @Getter
    @Setter
    protected int level = -1;

    private final BlockingQueue<LogHandlerRunnable> entries = new LinkedBlockingQueue<>();

    private final Thread logThread = new Thread() {

        @Override
        public void run()
        {
            while (!isInterrupted())
                try
                {
                    LogHandlerRunnable logHandlerRunnable = entries.take();

                    if (logHandlerRunnable != null)
                        logHandlerRunnable.call();

                } catch (Throwable e)
                {
                    break;
                }

            while (!entries.isEmpty())
                entries.poll().call();
        }
    };

    public AsyncLogProvider()
    {
        logThread.setDaemon(true);
        logThread.setPriority(Thread.MIN_PRIORITY);
        logThread.start();
    }

    @Override
    public ILogProvider log(LogEntry... logEntries)
    {
        if (logEntries == null) throw new NullPointerException("logEntries is null");

        for (LogEntry logEntry : logEntries)
            if (logEntry != null && (level == -1 || logEntry.getLogLevel().getLevel() <= level))
            {
                if (logEntry.getLogLevel().isAsync())
                    entries.offer(new LogHandlerRunnable(logEntry));
                else
                    new LogHandlerRunnable(logEntry).call();
            }

        return this;
    }

    @Override
    public boolean hasAsyncSupport()
    {
        return true;
    }

    @Override
    public synchronized ILogProvider addLogHandler(ILogHandler logHandler)
    {

        this.handlers.add(logHandler);
        return this;
    }

    @Override
    public synchronized ILogProvider addLogHandlers(ILogHandler... logHandlers)
    {

        for (ILogHandler logHandler : logHandlers) addLogHandler(logHandler);
        return this;
    }

    @Override
    public synchronized ILogProvider addLogHandlers(Iterable<ILogHandler> logHandlers)
    {

        for (ILogHandler logHandler : logHandlers) addLogHandler(logHandler);
        return this;
    }

    @Override
    public synchronized ILogProvider removeLogHandler(ILogHandler logHandler)
    {

        this.handlers.remove(logHandler);
        return this;
    }

    @Override
    public synchronized ILogProvider removeLogHandlers(ILogHandler... logHandlers)
    {

        for (ILogHandler logHandler : logHandlers) removeLogHandler(logHandler);
        return this;
    }

    @Override
    public synchronized ILogProvider removeLogHandlers(Iterable<ILogHandler> logHandlers)
    {

        for (ILogHandler logHandler : logHandlers) removeLogHandler(logHandler);
        return this;
    }

    @Override
    public Iterable<ILogHandler> getLogHandlers()
    {
        return new ArrayList<>(this.handlers);
    }

    @Override
    public boolean hasLogHandler(ILogHandler logHandler)
    {
        return this.handlers.contains(logHandler);
    }

    @Override
    public boolean hasLogHandlers(ILogHandler... logHandlers)
    {
        for (ILogHandler logHandler : logHandlers)
            if (!this.handlers.contains(logHandler)) return false;

        return true;
    }

    @Override
    public void close() throws Exception
    {
        for (ILogHandler logHandler : this.handlers) logHandler.close();

        this.logThread.interrupt();
        this.logThread.join();
        this.handlers.clear();
    }

    /*= ---------------------------------------------------- =*/

    @AllArgsConstructor
    public class LogHandlerRunnable implements Callable<Void> {

        private final LogEntry logEntry;

        @Override
        public Void call()
        {

            for (ILogHandler iLogHandler : handlers) iLogHandler.handle(logEntry);
            return null;
        }
    }

}