package de.dytanic.cloudnet.service.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.interfaces.INameable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ServiceState extends BasicDocPropertyable implements INameable {

    public static final ServiceState
            ONLINE = new ServiceState("online", 0, true, new Document()),
            INGAME = new ServiceState("ingame", 1, true, new Document()),
            OFFLINE = new ServiceState("offline", 2, false, new Document());

    /*= --------------------------------------------------------------------- =*/

    private final String name;

    private final int value;

    private final boolean runningService;

    public ServiceState(String name, int value, boolean runningService, Document properties)
    {
        this.name = name;
        this.value = value;
        this.properties = properties;
        this.runningService = runningService;
    }
}