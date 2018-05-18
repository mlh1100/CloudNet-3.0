package de.dytanic.cloudnet.service.process;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.service.util.IncludedTemplate;

import java.util.Collection;

abstract class AbstractServiceProcessBuilder<T extends AbstractServiceProcessMeta> {

    public abstract T build();

    protected String commandLine, name;

    protected Integer limitedHeapMemory, port;

    protected Collection<IncludedTemplate> templates = $.newArrayList();

    protected Collection<String> groups = $.newArrayList(), serviceDirectories = $.newArrayList();

    protected Document properties;

    public AbstractServiceProcessBuilder<T> newServiceDirectory(String serviceDirectory)
    {
        if (serviceDirectory != null) this.serviceDirectories.add(serviceDirectory);

        return this;
    }

    public Collection<String> serviceDirectories()
    {
        return serviceDirectories;
    }

    public AbstractServiceProcessBuilder<T> serviceDirectories(Collection<String> serviceDirectories)
    {
        if (serviceDirectories != null) this.serviceDirectories = serviceDirectories;

        return this;
    }

    public AbstractServiceProcessBuilder<T> port(int port)
    {
        this.port = port;

        return this;
    }

    public int port()
    {
        return port;
    }

    public AbstractServiceProcessBuilder<T> groups(Collection<String> groups)
    {
        if(groups != null) this.groups = groups;

        return this;
    }

    public AbstractServiceProcessBuilder<T> newGroup(String group)
    {
        this.groups.add(group);

        return this;
    }

    public Collection<String> groups()
    {
        return groups;
    }

    public AbstractServiceProcessBuilder<T> name(String name)
    {
        this.name = name;

        return this;
    }

    public String name()
    {
        return name;
    }

    public AbstractServiceProcessBuilder<T> properties(Document properties)
    {
        this.properties = properties;

        return this;
    }

    public Document properties()
    {
        return properties;
    }

    public AbstractServiceProcessBuilder<T> newTemplate(IncludedTemplate template)
    {
        if (template != null) this.templates.add(template);

        return this;
    }

    public AbstractServiceProcessBuilder<T> templates(Collection<IncludedTemplate> templates)
    {
        this.templates = templates;

        return this;
    }

    public Collection<IncludedTemplate> templates()
    {
        return templates;
    }

    public AbstractServiceProcessBuilder<T> commandLine(String commandLine)
    {
        this.commandLine = commandLine;

        return this;
    }

    public String commandLine()
    {
        return this.commandLine;
    }

    public AbstractServiceProcessBuilder<T> limitedHeapMemory(int limitedHeapMemory)
    {
        this.limitedHeapMemory = limitedHeapMemory;

        return this;
    }

    public int limitedHeapMemory()
    {
        return this.limitedHeapMemory;
    }

}