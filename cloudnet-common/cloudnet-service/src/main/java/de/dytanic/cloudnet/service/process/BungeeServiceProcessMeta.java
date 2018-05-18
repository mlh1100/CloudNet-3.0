package de.dytanic.cloudnet.service.process;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.service.util.IncludedTemplate;
import de.dytanic.cloudnet.util.Properties;
import lombok.Getter;

import java.util.Collection;

@Getter
public final class BungeeServiceProcessMeta extends AbstractServiceProcessMeta {

    protected Properties serverProperties;

    public BungeeServiceProcessMeta(String commandLine, String name, int limitedHeapMemory, int port, Collection<IncludedTemplate> templates, Collection<String> groups, Collection<String> servicesDirectories, Document properties, Properties serverProperties)
    {
        super(commandLine, name, limitedHeapMemory, port, templates, groups, servicesDirectories, properties);
        this.serverProperties = serverProperties;
    }

    public static BungeeServiceProcessBuilder builder()
    {
        return new BungeeServiceProcessBuilder();
    }

    public static class BungeeServiceProcessBuilder extends AbstractServiceProcessBuilder<BungeeServiceProcessMeta> {

        protected Properties serverProperties;

        public Properties serverProperties()
        {
            return serverProperties;
        }

        public BungeeServiceProcessBuilder newProperty(String key, String value)
        {
            this.serverProperties.put(key, value);

            return this;
        }

        public BungeeServiceProcessBuilder serverProperties(Properties serverProperties)
        {
            this.serverProperties = serverProperties;

            return this;
        }

        @Override
        public BungeeServiceProcessMeta build()
        {
            return new BungeeServiceProcessMeta(
                    commandLine,
                    name,
                    limitedHeapMemory == null ? 0 : limitedHeapMemory,
                    port == null ? 0 : port,
                    templates,
                    groups,
                    serviceDirectories,
                    properties == null ? new Document() : properties,
                    serverProperties == null ? new Properties() : serverProperties
            );
        }
    }

}