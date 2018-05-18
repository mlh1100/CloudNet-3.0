package de.dytanic.cloudnet.event.async;

import de.dytanic.cloudnet.event.Event;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;

/**
 * Created by Tareko on 20.12.2017.
 */
public abstract class AsyncEvent<E extends AsyncEvent> extends Event {

    protected Callback<E> callback;

    public AsyncEvent(Callback<E> done)
    {
        this.callback = done;
    }

    public void handle(E val)
    {
        callback.call(val);
    }

}