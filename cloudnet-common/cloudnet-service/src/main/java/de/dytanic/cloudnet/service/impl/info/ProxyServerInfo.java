package de.dytanic.cloudnet.service.impl.info;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.components.NetworkNode;
import de.dytanic.cloudnet.service.IServiceInfo;
import de.dytanic.cloudnet.service.ServiceId;
import de.dytanic.cloudnet.service.util.IncludedTemplate;
import de.dytanic.cloudnet.service.util.ServiceRuntimeState;
import de.dytanic.cloudnet.service.util.ServiceState;
import de.dytanic.cloudnet.service.util.ServiceType;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.*;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProxyServerInfo extends BasicDocPropertyable implements IServiceInfo {

    private ServiceId serviceId;

    private int onlineCount;

    private UUID[] onlinePlayers;

    private boolean online;

    private ServiceType serviceType;

    private ServiceState serviceState;

    private ServiceRuntimeState runtimeState;

    private Collection<IncludedTemplate> includedTemplates;

    private int currentHeapMemorySnapshot, limitedHeapMemory;

    private NetworkNode connectedNode;

    private Document properties;

    private HostAndPort address;

    private Collection<String> groups, serviceDirectories;

    private String commandLine;

    @Override
    public String getName()
    {
        return serviceId.getName();
    }
}