package de.dytanic.cloudnet.network.protocol.packet.codec;

import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.network.protocol.packet.IPacketSender;
import de.dytanic.cloudnet.network.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class PacketInDecoder extends ByteToMessageDecoder {

    private final IPacketSender packetSender;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception
    {
        ProtocolBuffer protocolBuffer = new ProtocolBuffer(byteBuf);
        Packet packet = new Packet();
        packet.read(protocolBuffer);

        packetSender.getPacketManager().handleIncomingPacket(packetSender, packet, protocolBuffer);
    }
}