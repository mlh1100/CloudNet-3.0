package de.dytanic.cloudnet.network.protocol.packet;

import java.util.concurrent.Future;

public interface IPacketSender {

    Future<?> sendPacket(Packet packet);

    IPacketManager getPacketManager();

    /* ===================================== */

    default void sendPacket(Packet... packets)
    {
        for(Packet packet : packets) sendPacket(packet);
    }
}