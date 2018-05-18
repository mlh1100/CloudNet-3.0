package de.dytanic.cloudnet.network.protocol.packet.codec;

import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import de.dytanic.cloudnet.network.protocol.packet.IPacketSender;
import de.dytanic.cloudnet.network.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PacketOutEncoder extends MessageToByteEncoder<Packet> {

    private final IPacketSender packetSender;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception
    {
        ProtocolBuffer protocolBuffer = new ProtocolBuffer(byteBuf);
        packetSender.getPacketManager().handlePreOutgoingPacket(packetSender, packet, protocolBuffer);
        packet.write(protocolBuffer);
        packetSender.getPacketManager().handlePostOutgoingPacket(packetSender, packet, protocolBuffer);
    }
}