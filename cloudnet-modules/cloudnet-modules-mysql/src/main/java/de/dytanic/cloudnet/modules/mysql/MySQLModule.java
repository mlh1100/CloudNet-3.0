package de.dytanic.cloudnet.modules.mysql;

import de.dytanic.cloudnet.database.AbstractDatabaseProvider;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.modules.mysql.database.MySQLDatabaseProvider;

public final class MySQLModule extends Module {

    @Override
    public void onLoad()
    {
        CloudNetDriver.getInstance().getServiceRegistry().registerService(AbstractDatabaseProvider.class, this, new MySQLDatabaseProvider());
    }

}