package de.dytanic.cloudnet.webserver.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyWebHandlerChannelHandler extends ChannelInboundHandlerAdapter {

    private final NettyWebServer nettyWebServer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if(msg instanceof HttpRequest)
        {
            HttpRequest httpRequest = (HttpRequest) msg;
            nettyWebServer.callHttpRequest(ctx, httpRequest);
            return;
        }

        ctx.channel().close();
    }
}