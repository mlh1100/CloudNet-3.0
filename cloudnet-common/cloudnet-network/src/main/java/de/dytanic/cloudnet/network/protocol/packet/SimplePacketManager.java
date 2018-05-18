package de.dytanic.cloudnet.network.protocol.packet;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.network.protocol.packet.util.DefaultPacketOptions;
import de.dytanic.cloudnet.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class SimplePacketManager implements IPacketManager {

    private final Map<Class<? extends Packet>, Integer> idRegistry = $.newConcurrentHashMap();

    private final Map<Integer, Collection<IPacketListener>> listenerRegistry = $.newConcurrentHashMap();

    private final Map<UUID, Pair<Object, Pair<Document, byte[]>>> synchronizedOperations = $.newConcurrentHashMap();

    public int getPacketId(Class<? extends Packet> clazz)
    {
        if (this.idRegistry.containsKey(clazz)) return this.idRegistry.get(clazz);

        return -1;
    }

    public void registerPacketIdentity(int id, Class<? extends Packet> packetClazz)
    {
        if (packetClazz == null) return;

        idRegistry.put(packetClazz, id);
    }

    public void registerListener(int id, IPacketListener packetListener)
    {
        if (packetListener == null) return;

        if (!listenerRegistry.containsKey(id))
            listenerRegistry.put(id, $.newArrayList());

        listenerRegistry.get(id).add(packetListener);
    }

    @Override
    public void unregisterPackets()
    {
        this.idRegistry.clear();
    }

    @Override
    public void unregisterHandlers()
    {
        this.listenerRegistry.clear();
    }

    public void register(int id, Class<? extends Packet> packetClazz, IPacketListener... listeners)
    {
        if (packetClazz == null || listeners == null) return;

        registerPacketIdentity(id, packetClazz);

        for (IPacketListener listener : listeners) registerListener(id, listener);
    }

    public Pair<Document, byte[]> executeQuery(IPacketSender packetSender, Packet packet) throws InterruptedException
    {
        UUID unqiueId = UUID.randomUUID();

        if (packet.header == null) packet.header = new Document();
        packet.header.setProperty(DefaultPacketOptions.REQUIRE_RESULT, unqiueId);

        Object object = new Object();
        Pair<Document, byte[]> entry = null;

        this.synchronizedOperations.put(unqiueId, new Pair<>(object, null));
        packetSender.sendPacket(packet);

        try
        {
            synchronized (object)
            {
                object.wait(TimeUnit.SECONDS.toMillis(5));
            }
            entry = this.synchronizedOperations.get(unqiueId).getSecond();

        } finally
        {
            this.synchronizedOperations.remove(unqiueId);
        }

        return entry;
    }

    @Override
    public void close()
    {
        this.idRegistry.clear();
        this.listenerRegistry.clear();

        for (Map.Entry<UUID, Pair<Object, Pair<Document, byte[]>>> entry : this.synchronizedOperations.entrySet())
        {
            synchronized (entry.getValue().getFirst())
            {
                entry.getValue().getFirst().notify();
            }

            this.synchronizedOperations.remove(entry.getKey());
        }
    }

    public Document fetchHeader(Packet packet)
    {
        return packet.header;
    }

    public byte[] fetchBody(Packet packet)
    {
        return packet.body;
    }

    /*= ---------------------------------------------------------------------------------------------- =*/

    public void handlePreOutgoingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception
    {
        if (packet == null) return;

        if (idRegistry.containsKey(packet.getClass()))
            packet.header.setProperty(DefaultPacketOptions.PACKET_ID, idRegistry.get(packet.getClass()));

    }

    public void handlePostOutgoingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception
    {
        if (packet == null) return;

        if (packet.header.hasProperty(DefaultPacketOptions.PACKET_ID))
        {
            call(packet.header.getProperty(DefaultPacketOptions.PACKET_ID), new Consumer<IPacketListener>() {

                @Override
                public void accept(IPacketListener packetListener)
                {
                    packetListener.handleOutgoing(packetSender, packet.header, packet.body);
                }
            });
        }
    }

    public void handleIncomingPacket(IPacketSender packetSender, Packet packet, ProtocolBuffer protocolBuffer) throws Exception
    {
        if (packet.header.hasProperty(DefaultPacketOptions.REQUIRE_RESULT) && this.synchronizedOperations.containsKey(
                packet.header.getProperty(DefaultPacketOptions.REQUIRE_RESULT)))
        {
            UUID uniqueId = packet.header.getProperty(DefaultPacketOptions.REQUIRE_RESULT);

            this.synchronizedOperations.get(uniqueId).setSecond(new Pair<>(packet.header, packet.body));

            Object object = this.synchronizedOperations.get(uniqueId).getFirst();

            synchronized (object)
            {
                object.notify();
            }
            return;
        }

        if (packet.header.hasProperty(DefaultPacketOptions.PACKET_ID))
        {
            call(packet.header.getProperty(DefaultPacketOptions.PACKET_ID), new Consumer<IPacketListener>() {
                @Override
                public void accept(IPacketListener packetListener)
                {
                    packetListener.handleIncoming(packetSender, packet.header, packet.body);
                }
            });
        }
    }

    private void call(int id, Consumer<IPacketListener> consumer)
    {
        if (this.listenerRegistry.containsKey(id))
            for (IPacketListener listener : this.listenerRegistry.get(id))
                consumer.accept(listener);
    }

}