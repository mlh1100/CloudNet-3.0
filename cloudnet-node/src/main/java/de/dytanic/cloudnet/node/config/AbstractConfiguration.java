package de.dytanic.cloudnet.node.config;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.util.HostAndPort;
import de.dytanic.cloudnet.webserver.WebConfig;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public abstract class AbstractConfiguration {

    protected WebConfig webConfig;

    protected Collection<String> moduleBlackList;

    protected Document properties;

    protected int cpuLimiter, memoryLimiter;

    protected UUID uniqueId;

    protected String nodeAccessKey;

    /*= ------------------------------------------ =*/

    //Runtime Config
    protected HostAndPort bind;

    protected String permissionService;

    /*= ------------------------------------------ =*/

    public abstract boolean needSetup();

    public abstract void setup(Document properties);

    public abstract void save();

    public abstract void load();

}