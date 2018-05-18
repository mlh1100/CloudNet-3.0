package de.dytanic.cloudnet.database;

import de.dytanic.cloudnet.document.Document;

public interface IDatabaseListener {

    void onInsert(IDatabase database, String key, Document document);

    void onUpdate(IDatabase database, String key, Document document);

    void onDelete(IDatabase database, String key);

}