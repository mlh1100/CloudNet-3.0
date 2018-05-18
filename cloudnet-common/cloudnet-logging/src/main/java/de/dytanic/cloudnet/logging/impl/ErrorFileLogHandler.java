package de.dytanic.cloudnet.logging.impl;

import de.dytanic.cloudnet.logging.LogEntry;
import de.dytanic.cloudnet.logging.util.AbstractLogHandler;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

public final class ErrorFileLogHandler extends AbstractLogHandler {

    private final File directory;

    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

    public ErrorFileLogHandler(String path)
    {
        this.directory = new File(path);
        this.directory.mkdirs();
    }

    @Override
    public void handle(LogEntry logEntry)
    {
        if (logEntry.getThrowable() != null)
        {
            File file = new File(this.directory, "error_" + dateFormat.format(logEntry.getTimeStamp()) + ".log");

            if (!file.exists())
            {
                try
                {
                    file.createNewFile();
                } catch (IOException e)
                {
                }
            }

            try (PrintStream printStream = new PrintStream(file))
            {
                printStream.println("=============================");
                printStream.println("- Timestamp " + new Timestamp(logEntry.getTimeStamp()).toString());
                printStream.println("- Java " + System.getProperty("java.version"));
                printStream.println("- User " + System.getProperty("user.name"));
                printStream.println("- OS " + System.getProperty("os.name"));
                printStream.println("- OS Version " + System.getProperty("os.version"));
                printStream.println("=============================");
                printStream.println("- Messages");

                for (String message : logEntry.getMessages())
                    if (message != null) printStream.println(message);

                printStream.println("- Thread " + logEntry.getThread());

                printStream.println("=============================");
                printStream.println();
                printStream.println("StackTrace:");
                printStream.println();

                printStream.println("=============================");
                printStream.println();
                printStream.println("System Properties");
                printStream.println();
                for(Map.Entry entry : System.getProperties().entrySet())
                {
                    printStream.println(entry.getKey() + "=" + entry.getValue());
                }

                logEntry.getThrowable().printStackTrace(printStream);

            } catch (IOException e)
            {
            }

        }
    }

    @Override
    public void close() throws Exception
    {

    }
}