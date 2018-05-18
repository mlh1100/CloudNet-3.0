package de.dytanic.cloudnet.node.config.defaults;

import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.node.config.AbstractConfiguration;
import de.dytanic.cloudnet.util.HostAndPort;
import de.dytanic.cloudnet.webserver.WebConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;

public final class CloudConfiguration extends AbstractConfiguration {

    private final Path pathConfigFile = Paths.get("config.json");

    public CloudConfiguration()
    {
        for (String key : new String[]{
                "local",
                "local/web",
                "local/config"
        })
            new File(key).mkdirs();
    }

    @Override
    public boolean needSetup()
    {
        return !Files.exists(pathConfigFile);
    }

    @Override
    public void setup(Document properties)
    {
        new Document()
                .append("uniqueId", UUID.randomUUID().toString())
                .append("access-key", $.randomString(32))
                .append("bind", new HostAndPort(properties.getString("node-host"), properties.getInt("node-port")))
                .append("webConfig",
                        new WebConfig(
                                properties.getBoolean("node-web-ssl"),
                                new HostAndPort(properties.getString("node-web-host"), properties.getInt("node-web-port"))
                        ))
                .append("memory-limit", properties.getInt("memory-limiter"))
                .append("start-service-cpu-limiter", properties.getInt("start-service-cpu-limiter"))
                .append("permissionService", "file")
                .append("module-blacklist", $.newArrayList())
                .save(pathConfigFile);
    }

    @Override
    public void save()
    {
        new Document()
                .append("uniqueId", UUID.randomUUID().toString())
                .append("access-key", this.nodeAccessKey)
                .append("bind", this.bind)
                .append("webConfig", this.webConfig)
                .append("memory-limit", this.memoryLimiter)
                .append("start-service-cpu-limiter", this.cpuLimiter)
                .append("permissionService", this.permissionService)
                .append("module-blacklist", this.moduleBlackList)
                .append("properties", new Document())
                .save(pathConfigFile);
    }

    @Override
    public void load()
    {
        Document document = Document.newDocument(pathConfigFile);

        this.uniqueId = UUID.fromString(document.getString("uniqueId"));
        this.nodeAccessKey = document.getString("access-key");

        this.bind = document.getObject("bind", HostAndPort.class);
        this.webConfig = document.getObject("webConfig", new TypeToken<WebConfig>() {
        }.getType());

        this.permissionService = document.getString("permissionService");
        this.memoryLimiter = document.getInt("memory-limit");
        this.cpuLimiter = document.getInt("start-service-cpu-limiter");
        this.properties = document.getDocument("properties");

        this.moduleBlackList = document.getObject("module-blacklist", new TypeToken<Collection<String>>() {
        }.getType());
    }
}