package de.dytanic.cloudnet.service;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.interfaces.INameable;
import de.dytanic.cloudnet.network.components.NetworkNode;
import lombok.*;

import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class ServiceId extends BasicDocPropertyable implements INameable {

    protected UUID uniqueId;

    protected String name;

    protected long id, timeStamp;

    protected NetworkNode node;

    public String getGameId()
    {
        if (uniqueId == null) return null;

        return uniqueId.toString().split("-")[0];
    }

}