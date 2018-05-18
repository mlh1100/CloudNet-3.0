package de.dytanic.cloudnet.database.h2;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.database.AbstractDatabaseProvider;
import de.dytanic.cloudnet.database.IDatabase;
import de.dytanic.cloudnet.scheduler.TaskScheduler;
import de.dytanic.cloudnet.util.AuthConfig;
import lombok.Getter;
import org.h2.Driver;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class H2DatabaseProvider extends AbstractDatabaseProvider {

    static
    {
        Driver.load();
    }

    @Getter
    private final String serviceName = "h2_db_provider";

    private final File h2dbFile;

    protected final Map<String, H2Database> cachedH2Databases = $.newConcurrentHashMap();

    protected final TaskScheduler taskScheduler = new TaskScheduler(1, null, null, 1L, false, TimeUnit.MINUTES.toMillis(1));

    private Connection connection;

    public H2DatabaseProvider(String h2File)
    {
        this.h2dbFile = new File(h2File);
    }

    @Override
    public IDatabase getDatabase(String name)
    {
        if (name == null) return null;
        name = name.toLowerCase();

        if (!cachedH2Databases.containsKey(name))
            this.cachedH2Databases.put(name, new H2Database(this, name, connection));

        return this.cachedH2Databases.get(name);
    }

    @Override
    public boolean init(AuthConfig authConfig) throws Exception
    {
        this.h2dbFile.getParentFile().mkdirs();
        this.connection = DriverManager.getConnection("jdbc:h2:" + h2dbFile.getAbsolutePath());

        return this.connection != null;
    }

    @Override
    public void close() throws Exception
    {
        for (H2Database h2Database : this.cachedH2Databases.values()) h2Database.close();

        this.taskScheduler.shutdown();
        this.connection.close();
    }

    @Override
    public boolean isRemoteService()
    {
        return false;
    }
}