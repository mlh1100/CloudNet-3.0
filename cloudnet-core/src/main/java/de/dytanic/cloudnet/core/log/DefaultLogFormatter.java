package de.dytanic.cloudnet.core.log;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.logging.LogEntry;
import de.dytanic.cloudnet.logging.util.IFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DefaultLogFormatter implements IFormatter {

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(LogEntry logEntry)
    {
        StringBuilder builder = new StringBuilder();
        if (logEntry.getThrowable() != null)
        {
            StringWriter writer = new StringWriter();
            logEntry.getThrowable().printStackTrace(new PrintWriter(writer));
            builder.append(writer).append($.LINE_SEPARATOR);
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String message : logEntry.getMessages())
            if (message != null)
                stringBuilder
                        .append("[")
                        .append(dateFormat.format(logEntry.getTimeStamp()))
                        .append("] ")
                        .append(logEntry.getLogLevel().getUpperName())
                        .append(": ")
                        .append(message)
                        .append($.LINE_SEPARATOR);


        stringBuilder.append(builder);

        return stringBuilder.toString();
    }
}