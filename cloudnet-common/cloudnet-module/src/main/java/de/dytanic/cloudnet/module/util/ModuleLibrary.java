package de.dytanic.cloudnet.module.util;

import de.dytanic.cloudnet.interfaces.INameable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;

@Getter
@AllArgsConstructor
public class ModuleLibrary implements INameable {

    private String name;

    private String url;

    public URL getResourceUrl() throws IOException
    {
        return new URL(url);
    }

}