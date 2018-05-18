package de.dytanic.cloudnet.network.protocol.packet;

import de.dytanic.cloudnet.document.Document;

public interface IPacketListener {

    void handleIncoming(IPacketSender packetSender, Document header, byte[] body);

    /*= ---------------------------------------------------------------------------------------------------- =*/

    default void handleOutgoing(IPacketSender packetSender, Document header, byte[] body)
    {

    }
}