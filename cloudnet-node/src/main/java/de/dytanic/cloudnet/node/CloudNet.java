package de.dytanic.cloudnet.node;

import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.console.command.ICommandMap;
import de.dytanic.cloudnet.core.AbstractCloudNetApplication;
import de.dytanic.cloudnet.core.console.MasterConsoleChannel;
import de.dytanic.cloudnet.core.log.EventTriggerLogHandler;
import de.dytanic.cloudnet.core.module.DefaultModuleHandler;
import de.dytanic.cloudnet.database.AbstractDatabaseProvider;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.language.LanguageManager;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.network.components.NetworkNode;
import de.dytanic.cloudnet.network.config.NodeListConfig;
import de.dytanic.cloudnet.node.command.*;
import de.dytanic.cloudnet.node.config.AbstractConfiguration;
import de.dytanic.cloudnet.node.config.defaults.CloudConfiguration;
import de.dytanic.cloudnet.node.web.webinterface.WIIndexWebHandler;
import de.dytanic.cloudnet.permissions.IPermissionProvider;
import de.dytanic.cloudnet.permissions.impl.FilePermissionProvider;
import de.dytanic.cloudnet.service.impl.storage.LocalStorageService;
import de.dytanic.cloudnet.service.storage.IStorageService;
import de.dytanic.cloudnet.util.Properties;
import de.dytanic.cloudnet.webserver.IWebServer;
import de.dytanic.cloudnet.webserver.impl.NettyWebServer;
import de.dytanic.cloudnet.webserver.webhandler.defaults.ResourceLoaderHttpWebHandler;
import lombok.Getter;

import java.nio.file.Paths;

@Getter
public final class CloudNet extends AbstractCloudNetApplication {

    @Getter
    private static CloudNet instance;

    /*= --------------------------------------------------------------------------- =*/
    //final fields
    private final String name = "CloudNet";

    /*= --------------------------------------------------------------------------- =*/
    //Changeable fields
    private IPermissionProvider permissionProvider;

    private AbstractDatabaseProvider databaseProvider;

    private IWebServer webServer;

    private AbstractConfiguration config;

    private NodeListConfig nodeListConfig;

    private NetworkNode networkNode;

    /*= --------------------------------------------------------------------------- =*/

    public CloudNet(String[] args, Properties properties, ILogProvider logProvider, IConsoleProvider consoleProvider, ICommandMap commandMap)
    {
        this.logProvider = logProvider;
        this.consoleProvider = consoleProvider;
        this.arguments = args;
        this.properties = properties;
        this.commandMap = commandMap;

        instance = this;
    }

    /*= --------------------------------------------------------------------------- =*/

    @Override
    public void setup() throws Exception
    {
        this.config = new CloudConfiguration();

        if (this.config.needSetup())
        {
            Document setupProperties = new Document();

            CloudNetSetup.createSetup(this.properties, this.logProvider, this.consoleProvider, setupProperties);
            this.config.setup(setupProperties);
        }
    }

    @Override
    public void start() throws Exception
    {
        this.logProvider.info(
                LanguageManager.getMessage("bootstrap-start-cloudnet")
                        .replace("%version%", CloudNet.class.getPackage().getImplementationVersion())
        );

        this.moduleManager.setRuntimeModuleHandler(new DefaultModuleHandler(this.eventManager, this.logProvider));
        this.consoleProvider.setConsoleChannel(new MasterConsoleChannel());
        this.logProvider.addLogHandler(new EventTriggerLogHandler());

        this.logProvider.info(LanguageManager.getMessage("bootstrap-start-register-services"));

        this.serviceRegistry.registerService(IPermissionProvider.class, this, new FilePermissionProvider(this.properties.getOrDefault("file-permission-provider-cfg-path", "local/config/permissions.json")));
        this.serviceRegistry.registerService(IStorageService.class, this, new LocalStorageService(this.properties.getOrDefault("local-storage-dir", "local/storage")));

        this.config.load();
        this.networkNode = new NetworkNode(this.config.getUniqueId(), this.config.getBind());

        this.nodeListConfig = new NodeListConfig(Paths.get(this.properties.getOrDefault("nodelist-config-path", "local/config/nodeList.json")));
        this.nodeListConfig.addNode(networkNode);

        this.logProvider.info(
                LanguageManager.getMessage("bootstrap-start-webserver")
                        .replace("%host%", this.config.getWebConfig().getBindAddress().getHost())
                        .replace("%port%", this.config.getWebConfig().getBindAddress().getPort() + "")
        );

        this.webServer = new NettyWebServer(this.config.getWebConfig());
        this.webServer.registerHandlers(
                new ResourceLoaderHttpWebHandler(
                        "webinterface",
                        new ResourceLoaderHttpWebHandler.ClassLoaderHttpWebHandlerResourceLoader("web", CloudNet.class.getClassLoader())
                ),
                new WIIndexWebHandler()
        );

        this.webServer.bind();
        this.loadResources();

        this.permissionProvider = this.serviceRegistry.getService(IPermissionProvider.class, this.config.getPermissionService());
        RUNNING = true;

        for (Module module : this.moduleManager.getEnabledModules())
            try
            {
                module.onEnable();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

        this.logProvider.info(LanguageManager.getMessage("bootstrap-start-cloudnet-complete"));
    }

    @Override
    public void reload()
    {
        this.unloadResources();

        //Config load
        this.config.load();

        this.loadResources();
    }

    @Override
    public void close()
    {
        if (!RUNNING) return;
        RUNNING = false;

        logProvider.info(LanguageManager.getMessage("shutdown-resources"));
        unloadResources();

        try
        {
            logProvider.info(LanguageManager.getMessage("shutdown-webserver"));

            this.webServer.close();

            logProvider.info(LanguageManager.getMessage("shutdown-logger"));
            logProvider.info("Thanks for using CloudNet! <3");

            this.logProvider.close();
            this.consoleProvider.close();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (!Thread.currentThread().getName().equals("CloudNet-Shutdown-Thread")) System.exit(0);
    }

    /*= --------------------------------------------------------------------------- =*/

    private void loadResources()
    {
        this.commandMap.registerCommand(
                new CommandHelp(),
                new CommandReload(),
                new CommandCreate(),
                new CommandDelete(),
                new CommandStop(),
                new CommandPermissions(),
                new CommandRepository(),
                new CommandModules(),
                new CommandClear()
        );

        this.moduleManager.detectModules(Paths.get(this.properties.get("module-dir")));
        this.moduleManager.loadModules();
    }

    private void unloadResources()
    {
        for (Module module : moduleManager.getEnabledModules())
            if (!module.getModuleConfig().isIgnoreReload())
                removeModule(module);

        commandMap.unregisterCommands();
        eventManager.clearListeners();
    }

    /*= --------------------------------------------------------------------------- =*/

    /*= --------------------------------------------------------------------------- =*/

}