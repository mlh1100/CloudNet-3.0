package de.dytanic.cloudnet.driver;

import de.dytanic.cloudnet.event.impl.DefaultEventManager;
import de.dytanic.cloudnet.event.IEventManager;
import de.dytanic.cloudnet.interfaces.INameable;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.impl.DefaultModuleManager;
import de.dytanic.cloudnet.permissions.IPermissionProvider;
import de.dytanic.cloudnet.registry.DefaultServiceRegistry;
import de.dytanic.cloudnet.registry.IServiceProvider;
import de.dytanic.cloudnet.registry.IServiceRegistry;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class CloudNetDriver implements AutoCloseable, INameable, IServiceProvider {

    @Getter
    @Setter
    private static CloudNetDriver instance;

    protected final IServiceRegistry serviceRegistry = new DefaultServiceRegistry();

    protected final IModuleManager moduleManager = new DefaultModuleManager();

    protected final IEventManager eventManager = new DefaultEventManager();

    public void removeModule(Module module)
    {
        if(module == null) return;

        this.serviceRegistry.removeServices(module);
        this.eventManager.unregisterListener(module);
        this.moduleManager.removeModule(module);
    }

    public abstract IPermissionProvider getPermissionProvider();

}