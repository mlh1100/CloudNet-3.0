package de.dytanic.cloudnet.module.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModuleRepositoryConfig extends BasicDocPropertyable {

    private final String url;

    private final boolean requireConnection, autoUpdate;

}