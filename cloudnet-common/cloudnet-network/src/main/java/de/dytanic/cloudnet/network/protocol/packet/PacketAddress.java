package de.dytanic.cloudnet.network.protocol.packet;

import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class PacketAddress {

    public static final Type TYPE = new TypeToken<PacketAddress>(){}.getType();

    protected UUID sender, receiver;

    protected HostAndPort senderAddress, receiverAddress;

}