package de.dytanic.cloudnet.scheduler;

import de.dytanic.cloudnet.scheduler.interfaces.Callback;

import java.util.concurrent.*;

public class TaskEntry<T> {

    protected volatile Callable<T> task;

    protected volatile T value = null;

    protected Callback<T> callback;

    protected long delayTimeOut, repeat, delay;

    protected boolean completed = false;

    private final TaskEntryFuture<T> future;

    public TaskEntry(Callable<T> task, Callback<T> complete, long delay, long repeat) {

        this.task = task;
        this.callback = complete;
        this.delay = delay;
        this.delayTimeOut = System.currentTimeMillis() + delay;
        this.repeat = repeat;
        this.future = new TaskEntryFuture<>(this, false);
    }



    protected void invoke() throws Exception {

        if (task == null)
            return;

        T val = task.call();

        value = val;

        if (callback != null)
            callback.call(val);

        if (repeat != -1 && repeat != 0) repeat--;

        if (repeat != 0)
            this.delayTimeOut = System.currentTimeMillis() + delay;
        else
        {
            completed = true;

            if(future.waits)
            {
                synchronized (future) {
                    future.notifyAll();
                }
            }
        }
    }



    public Callback<T> getCallback() {
        return callback;
    }



    public long getDelayTimeOut() {
        return delayTimeOut;
    }



    public long getRepeat() {
        return repeat;
    }



    protected TaskEntryFuture<T> drop() {
        return future;
    }



    public boolean isCompleted() {
        return completed;
    }

}