package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.network.protocol.packet.Packet;
import de.dytanic.cloudnet.network.protocol.packet.SimplePacketManager;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

public class PacketWriteAndReadTest {

    @Test
    public void testWriteAndRead()
    {
        Packet packet = new Packet(new Document("foo", "bar"), "Hello World".getBytes()), checkPacket = new Packet();

        ProtocolBuffer protocolBuffer = new ProtocolBuffer(Unpooled.buffer());

        packet.write(protocolBuffer);
        checkPacket.read(protocolBuffer);

        SimplePacketManager packetManager = new SimplePacketManager();

        Assert.assertTrue(packetManager.fetchHeader(checkPacket).contains("foo") && packetManager.fetchHeader(checkPacket).getString("foo").equals("bar"));
        Assert.assertTrue(new String(packetManager.fetchBody(checkPacket)).equals("Hello World"));
    }

}