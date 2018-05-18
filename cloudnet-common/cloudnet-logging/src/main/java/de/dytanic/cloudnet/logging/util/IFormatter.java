package de.dytanic.cloudnet.logging.util;

import de.dytanic.cloudnet.logging.LogEntry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public interface IFormatter {

    String format(LogEntry logEntry);

    static IFormatter defaultFormatter()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        return new IFormatter() {

            @Override
            public String format(LogEntry logEntry)
            {
                StringBuilder builder = new StringBuilder();
                if (logEntry.getThrowable() != null)
                {
                    StringWriter writer = new StringWriter();
                    logEntry.getThrowable().printStackTrace(new PrintWriter(writer));
                    builder.append(writer).append("\n");
                }

                StringBuilder stringBuilder = new StringBuilder()
                        .append("[")
                        .append(simpleDateFormat.format(System.currentTimeMillis()))
                        .append("/")
                        .append(System.getProperty("user.name"))
                        .append("] ")
                        .append(logEntry.getLogLevel().getUpperName())
                        .append(": ");


                for (String key : logEntry.getMessages()) stringBuilder.append(key).append("\n");

                stringBuilder.append(builder.toString());

                return stringBuilder.toString();
            }
        };
    }

}