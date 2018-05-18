package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.database.AbstractDatabaseProvider;
import de.dytanic.cloudnet.database.IDatabase;
import de.dytanic.cloudnet.database.IDatabaseListener;
import de.dytanic.cloudnet.database.h2.H2DatabaseProvider;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.util.AuthConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

public final class H2DatabaseProviderTest implements IDatabaseListener {

    protected String resultString;

    protected boolean value, geh;

    @Test
    public void testDatabaseProvider() throws Exception
    {
        AbstractDatabaseProvider databaseProvider = new H2DatabaseProvider("target/db-test/test");
        Assert.assertTrue(databaseProvider.init(new AuthConfig()));

        databaseProvider.registerListener(this);
        Assert.assertTrue(databaseProvider.getListeners().size() == 1);

        IDatabase database = databaseProvider.getDatabase("randomDataDatabase");
        Assert.assertTrue(database != null);

        Document document = new Document();
        for(int i = 0; i < 100; i++)
        {
            document.append("val", i).append("uniqueId", UUID.randomUUID()).append("random", new Random().nextLong());

            Assert.assertTrue(document.size() == 3);
            Assert.assertTrue(database.insert(i + "", document));
        }

        Assert.assertTrue(database.getDocuments().size() == 100);
        Assert.assertTrue(database.update(10 + "", new Document("val", 10)));
        Assert.assertTrue(database.select(10 + "").size() == 1);

        Assert.assertTrue(database.delete("10"));
        Assert.assertTrue(database.getDocuments().size() == 99);

        Assert.assertTrue(value && geh && resultString.equals("foobar"));

        databaseProvider.unregisterListener(this.getClass());
        Assert.assertTrue(databaseProvider.getListeners().size() == 0);

        databaseProvider.close();
    }

    @Override
    public void onInsert(IDatabase database, String key, Document document)
    {
        this.resultString = "foobar";
    }

    @Override
    public void onUpdate(IDatabase database, String key, Document document)
    {
        this.value = true;
    }

    @Override
    public void onDelete(IDatabase database, String key)
    {
        geh = true;
    }
}