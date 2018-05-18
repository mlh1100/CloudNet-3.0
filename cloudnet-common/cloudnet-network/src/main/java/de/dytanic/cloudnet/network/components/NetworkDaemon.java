package de.dytanic.cloudnet.network.components;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NetworkDaemon extends BasicDocPropertyable implements INetworkComponent {

    private UUID uniqueId, nodeUniqueId;

    private HostAndPort address, nodeAddress;

}