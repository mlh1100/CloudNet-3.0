package de.dytanic.cloudnet.network;

import de.dytanic.cloudnet.util.HostAndPort;

public interface Connectable {

    void connect(HostAndPort hostAndPort);

    default void connect(String address, int port)
    {
        connect(new HostAndPort(address, port));
    }

}