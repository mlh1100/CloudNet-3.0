package de.dytanic.cloudnet.webserver.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
final class NettyWebChannelInitializer extends ChannelInitializer<Channel> {

    private SslContext sslContext;

    private NettyWebServer nettyWebServer;

    @Override
    protected void initChannel(Channel channel)
    {
        if (sslContext != null) channel.pipeline().addFirst(sslContext.newHandler(channel.alloc()));
        channel.pipeline().addLast(new HttpServerCodec(), new HttpObjectAggregator(Integer.MAX_VALUE), new NettyWebHandlerChannelHandler(nettyWebServer));
    }
}