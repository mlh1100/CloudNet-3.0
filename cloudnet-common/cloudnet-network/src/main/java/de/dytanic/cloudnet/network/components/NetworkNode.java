package de.dytanic.cloudnet.network.components;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NetworkNode extends BasicDocPropertyable implements INetworkComponent {

    private final UUID uniqueId;

    private final HostAndPort address;

    @Override
    public UUID getNodeUniqueId()
    {
        return uniqueId;
    }

    @Override
    public HostAndPort getNodeAddress()
    {
        return address;
    }
}