package de.dytanic.cloudnet.network.components;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class NetworkNodeSnapshot extends BasicDocPropertyable implements IModulable {

    private UUID uniqueId;

    private HostAndPort address;

    private NetworkNode node;

    private Collection<String> ipWhitelist;

    private Collection<String> modules;

    private Collection<NetworkDaemon> currentDaemons;

}