package de.dytanic.cloudnet.network.protocol.packet;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.util.Pair;

public interface IPacketManager extends AutoCloseable {

    int getPacketId(Class<? extends Packet> clazz);

    void registerPacketIdentity(int id, Class<? extends Packet> packetClazz);

    void registerListener(int id, IPacketListener packetListener);

    void unregisterPackets();

    void unregisterHandlers();

    void register(int id, Class<? extends Packet> packetClazz, IPacketListener... listeners);

    Pair<Document, byte[]> executeQuery(IPacketSender packetSender, Packet packet) throws InterruptedException;

    void handlePreOutgoingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception;

    void handlePostOutgoingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception;

    void handleIncomingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception;

    Document fetchHeader(Packet packet);

    byte[] fetchBody(Packet packet);

}