package de.dytanic.cloudnet.database;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.interfaces.INameable;
import de.dytanic.cloudnet.scheduler.interfaces.Callback;
import de.dytanic.cloudnet.util.collection.IFilter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

public interface IDatabase extends INameable, AutoCloseable {

    boolean insert(String key, Document document);

    Future<Boolean> insertAsync(String key, Document document, Callback<Boolean> callback);

    boolean delete(String key);

    Future<Boolean> deleteAsync(String key, Callback<Boolean> callback);

    boolean update(String key, Document document);

    Future<Boolean> updateAsync(String key, Document document, Callback<Boolean> callback);

    Document select(String key);

    Future<Document> selectAsync(String key);

    IFilter<Document> filter(String pattern);

    Map<String, Document> entries();

    Collection<String> keys();

    Collection<Document> getDocuments();

    void clearDatabase();

    /*= -------------------------------------- =*/

    default boolean contains(String key)
    {
        return select(key) != null;
    }

    default Future<Boolean> deleteAsync(String key)
    {
        return deleteAsync(key, null);
    }

    default Future<Boolean> insertAsync(String key, Document document)
    {
        return insertAsync(key, document, null);
    }

    default Future<Boolean> updateAsync(String key, Document document)
    {
        return updateAsync(key, document, null);
    }

    default void insert(Map<String, Document> documents)
    {
        if(documents != null)
            for(Map.Entry<String, Document> entry : documents.entrySet())
                insert(entry.getKey(), entry.getValue());
    }

    default boolean delete(String... keys)
    {
        if (keys == null) return false;

        for (String key : keys)
            if (!delete(key)) return false;

        return true;
    }

}