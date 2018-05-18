package de.dytanic.cloudnet.modules.mysql.database;

import com.zaxxer.hikari.HikariDataSource;
import de.dytanic.cloudnet.database.AbstractDatabaseProvider;
import de.dytanic.cloudnet.database.IDatabase;
import de.dytanic.cloudnet.scheduler.TaskScheduler;
import de.dytanic.cloudnet.util.AuthConfig;
import lombok.Getter;

public final class MySQLDatabaseProvider extends AbstractDatabaseProvider {

    @Getter
    private final String serviceName = "mysql_db_provider";

    protected final HikariDataSource dataSource = new HikariDataSource();

    protected final TaskScheduler taskScheduler = new TaskScheduler(1);

    @Override
    public IDatabase getDatabase(String name)
    {
        if(name == null) return null;

        return new MySQLDatabase(name, this);
    }

    @Override
    public boolean init(AuthConfig authConfig) throws Exception
    {
        this.dataSource.setJdbcUrl("jdbc:mysql://" + authConfig.getProperties().getString("host") + ":" +
                authConfig.getProperties().getInt("port") + "/" + authConfig.getProperties().getString("database"));
        this.dataSource.setUsername(authConfig.getProperties().getString("username"));
        this.dataSource.setPassword(authConfig.getProperties().getString("password"));
        return true;
    }

    @Override
    public void close() throws Exception
    {
        taskScheduler.shutdown();
        dataSource.close();
    }

    @Override
    public boolean isRemoteService()
    {
        return true;
    }
}