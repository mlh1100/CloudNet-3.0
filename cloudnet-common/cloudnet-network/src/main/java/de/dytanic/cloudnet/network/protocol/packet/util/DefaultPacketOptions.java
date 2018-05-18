package de.dytanic.cloudnet.network.protocol.packet.util;

import de.dytanic.cloudnet.document.DocProperty;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.packet.PacketAddress;

import java.util.UUID;

public final class DefaultPacketOptions {

    private DefaultPacketOptions()
    {
    }

    private static final String
            PROPERTY_PACKET_ID = "$_id",
            PROPERTY_PACKET_ADDRESS = "$_packetAddress",
            PROPERTY_REQUIRE_RESULT = "$_requireResult";

    public static final DocProperty<Integer> PACKET_ID = new DocProperty<>(
            DefaultPacketOptions::appendPacketId,
            DefaultPacketOptions::getPacketId,
            DefaultPacketOptions::removePacketId
    );

    public static final DocProperty<PacketAddress> PACKET_ADDRESS = new DocProperty<>(
            DefaultPacketOptions::appendPacketAddress,
            DefaultPacketOptions::getPacketAddress,
            DefaultPacketOptions::removePacketAddress
    );

    public static final DocProperty<UUID> REQUIRE_RESULT = new DocProperty<>(
            DefaultPacketOptions::appendRequireResult,
            DefaultPacketOptions::getRequireResult,
            DefaultPacketOptions::removeRequireResult
    );

    /*= -------------------------------------------------------------------------- =*/

    //PacketId
    private static void appendPacketId(Integer value, Document document)
    {
        if (value == null) return;

        document.append(PROPERTY_PACKET_ID, value);
    }

    private static Integer getPacketId(Document document)
    {
        if (!document.contains(PROPERTY_PACKET_ID)) return null;

        return document.getInt(PROPERTY_PACKET_ID);
    }

    private static void removePacketId(Document document)
    {
        document.remove(PROPERTY_PACKET_ID);
    }

    /*= -------------------------------------------------------------------------- =*/

    //PacketAddress
    private static void appendPacketAddress(PacketAddress packetAddress, Document document)
    {
        if (packetAddress == null) return;

        document.append(PROPERTY_PACKET_ADDRESS, packetAddress);
    }

    private static PacketAddress getPacketAddress(Document document)
    {
        if (!document.contains(PROPERTY_PACKET_ADDRESS)) return null;

        return document.getObject(PROPERTY_PACKET_ADDRESS, PacketAddress.TYPE);
    }

    private static void removePacketAddress(Document document)
    {
        document.remove(PROPERTY_PACKET_ADDRESS);
    }

    /*= -------------------------------------------------------------------------- =*/

    //RequireResult
    private static void appendRequireResult(UUID value, Document document)
    {
        if (value == null) return;

        document.append(PROPERTY_REQUIRE_RESULT, value);
    }

    private static UUID getRequireResult(Document document)
    {
        if (!document.contains(PROPERTY_REQUIRE_RESULT)) return null;

        return document.getObject(PROPERTY_REQUIRE_RESULT, UUID.class);
    }

    private static void removeRequireResult(Document document)
    {
        document.remove(PROPERTY_REQUIRE_RESULT);
    }

    /*= -------------------------------------------------------------------------- =*/

}