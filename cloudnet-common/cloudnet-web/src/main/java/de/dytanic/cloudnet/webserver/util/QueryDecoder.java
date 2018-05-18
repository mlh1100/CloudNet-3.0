/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnet.webserver.util;

import de.dytanic.cloudnet.$;
import lombok.Getter;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Tareko on 16.09.2017.
 */
@Getter
public class QueryDecoder {

    private final Map<String, String> queryParams = new WeakHashMap<>();

    public QueryDecoder(String query)
    {
        if (query == null) return;
        if(query.length() == 0 || query.isEmpty() || query.equals("?")) return;
        if(query.startsWith("?")) query = query.replace("?", $.EMPTY_STRING);

        for (String input : query.split("&"))
        {
            String[] value = input.split("=");
            queryParams.put(value[0], value[1]);
        }
    }
}