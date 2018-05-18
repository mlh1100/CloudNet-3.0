package de.dytanic.cloudnet.network.protocol;

public interface IProtocolListener {

    void write(ProtocolBuffer protocolBuffer) throws Exception;

    void read(ProtocolBuffer protocolBuffer) throws Exception;

}