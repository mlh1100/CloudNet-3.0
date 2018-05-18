package de.dytanic.cloudnet.service;

import de.dytanic.cloudnet.document.IDocPropertyable;
import de.dytanic.cloudnet.interfaces.INameable;
import de.dytanic.cloudnet.network.components.NetworkNode;
import de.dytanic.cloudnet.service.group.IGroupable;
import de.dytanic.cloudnet.service.util.IncludedTemplate;
import de.dytanic.cloudnet.service.util.ServiceRuntimeState;
import de.dytanic.cloudnet.service.util.ServiceState;
import de.dytanic.cloudnet.service.util.ServiceType;
import de.dytanic.cloudnet.util.HostAndPort;

import java.util.Collection;
import java.util.UUID;

public interface IServiceInfo extends INameable, IGroupable, IDocPropertyable {

    ServiceId getServiceId();

    boolean isOnline();

    ServiceType getServiceType();

    ServiceState getServiceState();

    ServiceRuntimeState getRuntimeState();

    Collection<IncludedTemplate> getIncludedTemplates();

    Collection<String> getServiceDirectories();

    int getCurrentHeapMemorySnapshot();

    int getLimitedHeapMemory();

    NetworkNode getConnectedNode();

    HostAndPort getAddress();

    String getCommandLine();

    /*= ------------------------------------------------------ =*/

    default UUID getUniqueId()
    {
        return getServiceId().getUniqueId();
    }

    default NetworkNode getProvidedNode()
    {
        return getServiceId().getNode();
    }

}