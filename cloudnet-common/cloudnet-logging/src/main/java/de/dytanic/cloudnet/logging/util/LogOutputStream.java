package de.dytanic.cloudnet.logging.util;

import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.logging.LogLevel;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class LogOutputStream extends ByteArrayOutputStream {

    protected ILogProvider logProvider;

    protected LogLevel logLevel;

    @Override
    public void flush() throws IOException
    {
        String input = toString(StandardCharsets.UTF_8.name());
        this.reset();

        if(input != null && !input.isEmpty() && !input.equals(" ") && !input.equals(System.lineSeparator()))
            logProvider.log(logLevel, input);
    }
}