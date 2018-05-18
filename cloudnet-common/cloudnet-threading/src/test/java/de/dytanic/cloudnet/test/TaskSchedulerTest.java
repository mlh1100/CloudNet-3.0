package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.scheduler.TaskScheduler;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TaskSchedulerTest implements Callable<String> {

    @Test
    public void testTaskScheduler() throws Exception
    {

        TaskScheduler taskScheduler = new TaskScheduler(4, null, null, 1L, false, 15000);

        Assert.assertTrue(taskScheduler.getMaxThreads() == 4);
        Assert.assertTrue(taskScheduler.chargeThreadLimit((short) 2).getMaxThreads() == 6);

        Future<String> x = taskScheduler.schedule(this);

        Assert.assertTrue(taskScheduler.getCurrentThreadSize() == 1);

        Future<String> y = taskScheduler.schedule(this);

        Assert.assertTrue(taskScheduler.getCurrentThreadSize() == 2);

        String result = x.get();
        Assert.assertTrue(result.equals("Hello World"));

        y.get();
    }

    @Override
    public String call() throws Exception
    {
        for (int i = 0; i++ < 5; Thread.sleep(2)) ;

        return "Hello World";
    }
}