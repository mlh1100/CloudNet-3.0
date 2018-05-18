package de.dytanic.cloudnet.network;

import de.dytanic.cloudnet.util.HostAndPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.Getter;

public final class NetworkServer {

    @Getter
    private final HostAndPort hostAndPort;

    private final EventLoopGroup
            bossEventLoopGroup = NetworkUtils.eventLoopGroup(),
            childEventLoopGroup = NetworkUtils.eventLoopGroup();

    /*= ---------------------------------------------------------------------------- =*/

    private ChannelFuture channelFuture;

    public NetworkServer(HostAndPort hostAndPort)
    {
        this.hostAndPort = hostAndPort;
    }

    public void bind() throws Exception
    {
        this.channelFuture = new ServerBootstrap()
                .channel(NetworkUtils.serverSocketChannel())
                .group(bossEventLoopGroup, childEventLoopGroup)
                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception
                    {

                    }
                })
                .bind()
                .sync()
                .channel()
                .closeFuture();
    }

}