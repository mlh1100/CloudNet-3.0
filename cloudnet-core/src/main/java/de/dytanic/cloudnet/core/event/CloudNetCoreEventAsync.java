package de.dytanic.cloudnet.core.event;

import de.dytanic.cloudnet.event.async.AsyncEvent;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;

public abstract class CloudNetCoreEventAsync<T extends CloudNetCoreEventAsync> extends AsyncEvent<T> implements ICloudNetCoreEvent {

    public CloudNetCoreEventAsync(Callback<T> done)
    {
        super(done);
    }
}