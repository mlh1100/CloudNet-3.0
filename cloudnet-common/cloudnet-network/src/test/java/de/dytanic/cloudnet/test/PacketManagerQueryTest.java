package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.network.protocol.packet.IPacketSender;
import de.dytanic.cloudnet.network.protocol.packet.Packet;
import de.dytanic.cloudnet.network.protocol.packet.SimplePacketManager;
import de.dytanic.cloudnet.util.Pair;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Future;

public class PacketManagerQueryTest implements IPacketSender {

    private SimplePacketManager packetManager = new SimplePacketManager();

    @Test
    public void testSyncOperation() throws Exception
    {
        Packet packet = new Packet(new Document("foo", "bar"), new byte[0]);

        Pair<Document, byte[]> pair = packetManager.executeQuery(this, packet);

        Assert.assertTrue(pair != null);
        Assert.assertTrue(pair.getFirst() != null);
        Assert.assertTrue(pair.getSecond() != null);

        Assert.assertTrue(pair.getFirst().getString("foo").equals("bar"));
    }

    @Override
    public Future<?> sendPacket(Packet packet)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    packetManager.handleIncomingPacket(PacketManagerQueryTest.this, packet, new ProtocolBuffer(Unpooled.buffer()));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return null;
    }

    @Override
    public void sendPacket(Packet... packets)
    {

    }

    @Override
    public SimplePacketManager getPacketManager()
    {
        return null;
    }
}