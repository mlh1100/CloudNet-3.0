package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.logging.ILogHandler;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.logging.LogEntry;
import de.dytanic.cloudnet.logging.LogLevel;
import de.dytanic.cloudnet.logging.impl.AsyncLogProvider;
import org.junit.Assert;
import org.junit.Test;

public class AsyncLogProviderTest {

    private String data;

    private boolean closed = false;

    @Test
    public void testLogging() throws Exception
    {

        ILogProvider logProvider = new AsyncLogProvider();

        LogHandler logHandler = new LogHandler();

        Assert.assertTrue(logProvider.addLogHandler(logHandler) != null);
        Assert.assertTrue(logProvider.getLogHandlers().iterator().hasNext());
        Assert.assertTrue(logProvider.hasAsyncSupport());

        logProvider.log(LogLevel.MODULE, AsyncLogProviderTest.class, new String[]{"My log message!", "foo", "bar"});

        Assert.assertTrue(data == null);

        Thread.sleep(5);
        Assert.assertTrue(data != null && data.equals("My log message!"));

        logProvider.close();

        Assert.assertTrue(closed);
    }

    private class LogHandler implements ILogHandler
    {

        @Override
        public void handle(LogEntry logEntry)
        {
            data = logEntry.getMessages()[0];

            Assert.assertTrue("data transfer", data != null);
        }

        @Override
        public void close()
        {
            closed = true;
        }
    }

}