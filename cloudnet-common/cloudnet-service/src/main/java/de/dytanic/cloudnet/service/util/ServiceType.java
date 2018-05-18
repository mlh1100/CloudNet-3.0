package de.dytanic.cloudnet.service.util;

import de.dytanic.cloudnet.interfaces.INameable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ServiceType implements INameable {

    private String name;

    private boolean minecraftServer;

}