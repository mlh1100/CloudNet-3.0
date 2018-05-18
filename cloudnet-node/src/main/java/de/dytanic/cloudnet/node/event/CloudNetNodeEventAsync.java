package de.dytanic.cloudnet.node.event;

import de.dytanic.cloudnet.event.async.AsyncEvent;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;

/**
 * This Class marks all async Events which only exist on the Master platform
 */
public abstract class CloudNetNodeEventAsync<T extends CloudNetNodeEventAsync> extends AsyncEvent<T> implements ICloudNetNodeEvent {

    public CloudNetNodeEventAsync(Callback<T> done)
    {
        super(done);
    }
}