package de.dytanic.cloudnet.database.h2;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.database.IDatabase;
import de.dytanic.cloudnet.database.IDatabaseListener;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;
import de.dytanic.cloudnet.util.collection.IFilter;
import de.dytanic.cloudnet.util.collection.QueueFilter;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * H2Database implementation for the DatabaseLibrary
 */
final class H2Database implements IDatabase {

    @Getter
    private final String name;

    private final Connection connection;

    private final H2DatabaseProvider databaseProvider;

    public H2Database(H2DatabaseProvider databaseProvider, String name, Connection connection)
    {
        this.databaseProvider = databaseProvider;
        this.connection = connection;
        this.name = name;

        createTable(name);
    }

    @Override
    public boolean insert(String key, Document document)
    {
        if (key == null || document == null) return false;

        boolean result = sqlUpdateStatement("INSERT INTO " + name + " (Name, Document) VALUES (?, ?);", key, document.toJson());

        databaseProvider.call(new Consumer<IDatabaseListener>() {
            @Override
            public void accept(IDatabaseListener listener)
            {
                listener.onInsert(H2Database.this, key, document);
            }
        });

        return result;
    }

    @Override
    public Future<Boolean> insertAsync(String key, Document document, Callback<Boolean> callback)
    {
        return databaseProvider.taskScheduler.schedule(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception
            {
                boolean value = insert(key, document);
                if (callback != null) callback.call(value);

                return value;
            }
        });
    }

    @Override
    public boolean delete(String key)
    {
        boolean result = sqlUpdateStatement("DELETE FROM " + name + " WHERE Name=?", key);

        databaseProvider.call(new Consumer<IDatabaseListener>() {
            @Override
            public void accept(IDatabaseListener listener)
            {
                listener.onDelete(H2Database.this, key);
            }
        });

        return result;
    }

    @Override
    public Future<Boolean> deleteAsync(String key, Callback<Boolean> callback)
    {
        return databaseProvider.taskScheduler.schedule(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception
            {
                boolean value = delete(key);
                if (callback != null) callback.call(value);

                return value;
            }
        });
    }

    @Override
    public boolean update(String key, Document document)
    {
        if (key == null || document == null) return false;

        boolean result = sqlUpdateStatement("UPDATE " + name + " SET Document=? WHERE Name=?", document.toJson(), key);

        databaseProvider.call(new Consumer<IDatabaseListener>() {
            @Override
            public void accept(IDatabaseListener listener)
            {
                listener.onUpdate(H2Database.this, key, document);
            }
        });

        return result;
    }

    @Override
    public Future<Boolean> updateAsync(String key, Document document, Callback<Boolean> callback)
    {
        return databaseProvider.taskScheduler.schedule(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception
            {
                boolean value = update(key, document);
                if (callback != null) callback.call(value);

                return value;
            }
        });
    }

    @Override
    public Document select(String key)
    {
        if (key == null) return null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Document FROM " + name + " WHERE Name=?"))
        {
            preparedStatement.setString(1, key);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next()) return Document.newDocument(resultSet.getString("Document"));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public IFilter<Document> filter(String pattern)
    {
        IFilter<Document> filter = null;

        if (pattern == null) return null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT Document FROM " + name + " WHERE Name LIKE " + pattern.replace("*", "%")
        ))
        {
            preparedStatement.setString(1, pattern);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                Collection<Document> list = $.newArrayList();

                while (resultSet.next()) list.add(Document.newDocument(resultSet.getString("Document")));

                filter = new QueueFilter<>(list);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return filter;
    }

    @Override
    public Future<Document> selectAsync(String key)
    {
        return databaseProvider.taskScheduler.schedule(new Callable<Document>() {
            @Override
            public Document call() throws Exception
            {
                return select(key);
            }
        });
    }

    @Override
    public Map<String, Document> entries()
    {
        Map<String, Document> entries = $.newHashMap();

        String columnName = "Name", columnDocumet = "Document";
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + this.name);
             ResultSet resultSet = preparedStatement.executeQuery())
        {

            while (resultSet.next())
                entries.put(resultSet.getString(columnName), Document.newDocument(resultSet.getString(columnDocumet)));

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return entries;
    }

    @Override
    public Collection<String> keys()
    {
        Collection<String> keys = $.newArrayList();

        String columnName = "Name";
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM " + this.name);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) keys.add(resultSet.getString(columnName));

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return keys;
    }

    @Override
    public Collection<Document> getDocuments()
    {
        Collection<Document> keys = $.newArrayList();

        String columnName = "Document";
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Document FROM " + this.name);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) keys.add(Document.newDocument(resultSet.getString(columnName)));

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return keys;
    }

    @Override
    public void close() throws Exception
    {
        this.databaseProvider.cachedH2Databases.remove(name);
    }

    @Override
    public void clearDatabase()
    {
        Collection<String> keys = keys();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + name + " WHERE NAME=?"))
        {
            for (String key : keys)
            {
                preparedStatement.setString(1, key);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /*= ---------------------------------------------------------------------- =*/

    public boolean sqlUpdateStatement(String query, Object... objects)
    {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query))
        {
            short i = 1;
            for (Object object : objects)
                preparedStatement.setString(i++, object.toString());

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void createTable(String name)
    {
        sqlUpdateStatement("CREATE TABLE IF NOT EXISTS " + name + "(Name VARCHAR(255), Document TEXT);");
    }

}