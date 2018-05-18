package de.dytanic.cloudnet.service.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.interfaces.INameable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncludedTemplate extends BasicDocPropertyable implements INameable {

    private String name;

    private String storageService;

}