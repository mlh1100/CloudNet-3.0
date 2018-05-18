package de.dytanic.cloudnet.logging;

public interface ILogHandlerProvider {

    ILogProvider addLogHandler(ILogHandler logHandler);

    ILogProvider addLogHandlers(ILogHandler... logHandlers);

    ILogProvider addLogHandlers(Iterable<ILogHandler> logHandlers);

    ILogProvider removeLogHandler(ILogHandler logHandler);

    ILogProvider removeLogHandlers(ILogHandler... logHandlers);

    ILogProvider removeLogHandlers(Iterable<ILogHandler> logHandlers);

    Iterable<ILogHandler> getLogHandlers();

    boolean hasLogHandler(ILogHandler logHandler);

    boolean hasLogHandlers(ILogHandler... logHandlers);

}