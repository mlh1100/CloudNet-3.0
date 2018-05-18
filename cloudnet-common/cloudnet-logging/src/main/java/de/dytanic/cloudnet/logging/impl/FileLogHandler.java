package de.dytanic.cloudnet.logging.impl;

import de.dytanic.cloudnet.logging.LogEntry;
import de.dytanic.cloudnet.logging.util.AbstractLogHandler;
import de.dytanic.cloudnet.logging.util.IFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public final class FileLogHandler extends AbstractLogHandler {

    public static final long SIZE_16MB = 16000000L;

    /*= -------------------------------- =*/

    private final File directory;

    private final String pattern;

    private final long maxBytes;

    /*= -------------------------------- =*/

    private File entry;

    private PrintWriter printWriter;

    private long writternBytes = 0L;

    private int index = 0;

    public FileLogHandler(File directory, String pattern, long maxBytes)
    {
        if (directory == null) directory = new File("logs");

        this.directory = directory;
        this.directory.mkdirs();

        this.pattern = pattern;
        this.maxBytes = maxBytes;

        selectLogFile();
    }

    @Override
    public void handle(LogEntry logEntry)
    {
        if (getFormatter() == null) setFormatter(IFormatter.defaultFormatter());
        if (entry == null) selectLogFile();
        if (entry.length() > maxBytes) selectLogFile();

        String formatted = getFormatter().format(logEntry);
        this.writternBytes = writternBytes + formatted.getBytes(StandardCharsets.UTF_8).length;

        if (this.writternBytes > maxBytes) selectLogFile();

        printWriter.write(formatted);
        printWriter.flush();
    }

    @Override
    public void close() throws Exception
    {

        index = 0;
        printWriter.close();
    }

    private void selectLogFile()
    {
        if (printWriter != null) printWriter.close();
        if (writternBytes != 0L) writternBytes = 0L;

        entry = null;
        File file;

        while (entry == null)
        {
            file = new File(directory, pattern + "." + index);

            try
            {

                if (!file.exists()) file.createNewFile();

                if (file.length() < maxBytes)
                {
                    this.entry = file;
                    this.printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.entry), StandardCharsets.UTF_8));
                    break;
                }

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            index++;
        }

        index = 0;
    }

}