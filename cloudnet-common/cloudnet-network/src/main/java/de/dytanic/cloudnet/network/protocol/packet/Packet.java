package de.dytanic.cloudnet.network.protocol.packet;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.protocol.IProtocolListener;
import de.dytanic.cloudnet.network.protocol.ProtocolBuffer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Packet implements IProtocolListener {

    protected Document header;

    protected byte[] body;

    public Packet(Document header, byte[] body)
    {
        this.header = header;
        this.body = body;
    }

    @Override
    public void write(ProtocolBuffer protocolBuffer)
    {
        if (header == null) return;
        if (body == null) body = new byte[0];

        //Write Header
        protocolBuffer.writeString(this.header.toJson());

        //Write Body
        protocolBuffer.writeVarInt(this.body.length);
        protocolBuffer.writeBytes(this.body);
    }

    @Override
    public void read(ProtocolBuffer protocolBuffer)
    {
        if (protocolBuffer.readableBytes() == 0) return;

        //Read Header
        this.header = Document.newDocument(protocolBuffer.readString());

        //Read Body
        this.body = protocolBuffer.toByteArray(protocolBuffer.readVarInt());
    }
}