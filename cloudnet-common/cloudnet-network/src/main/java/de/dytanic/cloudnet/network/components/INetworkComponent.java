package de.dytanic.cloudnet.network.components;

import de.dytanic.cloudnet.util.HostAndPort;

import java.util.UUID;

public interface INetworkComponent {

    UUID getUniqueId();

    UUID getNodeUniqueId();

    HostAndPort getAddress();

    HostAndPort getNodeAddress();

}