package de.dytanic.cloudnet.network.components;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class NetworkDaemonSnapshot extends BasicDocPropertyable implements IModulable {

    private UUID uniqueId;

    private HostAndPort daemonAddress, nodeAddress;

    private NetworkDaemon daemon;

    private int cores, limitedMemory;

    private Collection<String> modules;

}