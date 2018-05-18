package de.dytanic.cloudnet.service.group;

import de.dytanic.cloudnet.interfaces.INameable;
import lombok.Data;

@Data
public class ServerGroup implements INameable {

    private String name;

}