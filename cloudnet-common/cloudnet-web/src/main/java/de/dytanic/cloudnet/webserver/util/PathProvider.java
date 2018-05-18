package de.dytanic.cloudnet.webserver.util;

import de.dytanic.cloudnet.util.WrappedMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Tareko on 07.10.2017.
 */
@Getter
@AllArgsConstructor
public class PathProvider {

    private String path;

    private String[] pathAliases;

    private WrappedMap pathParameters;

}