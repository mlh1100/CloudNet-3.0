package de.dytanic.cloudnet.webserver.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.scheduler.TaskScheduler;
import de.dytanic.cloudnet.util.WrappedMap;
import de.dytanic.cloudnet.webserver.IWebServer;
import de.dytanic.cloudnet.webserver.WebConfig;
import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.WebHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Getter
public final class NettyWebServer implements IWebServer {

    private final TaskScheduler taskScheduler = new TaskScheduler(2);

    private final EventLoopGroup
            workerEventLoopGroup = selectGroup(),
            bossEventLoopGroup = selectGroup();

    private final Collection<WebHandler> registeredWebHandlers = new CopyOnWriteArrayList<>();

    /*= -------------------------------------------------------------------------------- =*/

    private final WebConfig config;

    private ChannelFuture channelFuture;

    private SslContext sslContext;

    public NettyWebServer(WebConfig config)
    {
        this.config = config;
    }

    @Override
    public IWebServer bind()
    {
        if (config.isSsl())
        {
            try
            {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        try
        {
            this.channelFuture = new ServerBootstrap()
                    .childOption(ChannelOption.IP_TOS, 24)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.AUTO_READ, true)
                    .group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new NettyWebChannelInitializer(sslContext, this))
                    .bind(this.config.getBindAddress().getHost(), this.config.getBindAddress().getPort()).sync().channel().closeFuture();

        } catch (Exception ex)
        {
            ex.printStackTrace();

            try
            {
                close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        return this;
    }

    @Override
    public void close() throws Exception
    {
        this.clearHandlers();

        if (channelFuture != null)
        {
            this.channelFuture.channel().close();
            this.channelFuture.cancel(true);
        }

        this.bossEventLoopGroup.shutdownGracefully();
        this.workerEventLoopGroup.shutdownGracefully();

        this.bossEventLoopGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        this.workerEventLoopGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    @Override
    public void registerHandler(WebHandler webHandler)
    {
        this.registeredWebHandlers.add(webHandler);
    }

    @Override
    public void unregisterHandler(Class<? extends WebHandler> webHandlerClazz)
    {
        WebHandler webHandler = $.filterFirst(this.registeredWebHandlers, new Predicate<WebHandler>() {
            @Override
            public boolean test(WebHandler webHandler)
            {
                return webHandler.getClass().equals(webHandlerClazz);
            }
        });

        if (webHandler != null) this.registeredWebHandlers.remove(webHandler);
    }

    @Override
    public Collection<WebHandler> getHandlers()
    {
        return $.newArrayList(this.registeredWebHandlers);
    }

    /*= ---------------------------------------------------------------------- =*/

    public void callHttpRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception
    {
        NettyWebContext webContext = new NettyWebContext(channelHandlerContext, httpRequest);
        String path = webContext.getPath();

        if (path == null) path = $.SLASH_STRING;
        if (path.endsWith($.SLASH_STRING)) path = path.substring(0, path.length() - 1);

        String[] array = path.replaceFirst($.SLASH_STRING, $.EMPTY_STRING).split($.SLASH_STRING);

        for (WebHandler webHandler : getAvailableWebHandlers(path))
        {
            String[] pathArray = webHandler.getPath().startsWith($.SLASH_STRING) ? webHandler.getPath().replaceFirst($.SLASH_STRING,
                    $.EMPTY_STRING).split($.SLASH_STRING) : webHandler.getPath().split($.SLASH_STRING);

            WrappedMap wrappedMap = new WrappedMap();
            List<String> alias = $.newArrayList();

            for (short i = 0; i < array.length; i++)
            {
                //TODO: ///////////////////////////////////////// Insert * alias
                if (alias.size() == 0)
                {
                    if (pathArray[i].equals("*"))
                    {
                        alias.add(array[i]);
                        continue;
                    }

                } else
                {
                    alias.add(array[i]);
                    continue;
                }

                if (pathArray[i].startsWith("{") && pathArray[i].endsWith("}"))
                    wrappedMap.append(pathArray[i].replace("{", $.EMPTY_STRING).replace("}", $.EMPTY_STRING), array[i]);
            }

            try
            {
                webHandler.onHttpRequest(webContext, new PathProvider(path, alias.toArray(new String[0]), wrappedMap));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        webContext.close();
        channelHandlerContext.channel().writeAndFlush(webContext.getDefaultFullHttpResponse()).addListener(ChannelFutureListener.CLOSE);
    }

    private EventLoopGroup selectGroup()
    {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    public Collection<WebHandler> getAvailableWebHandlers(String path)
    {
        String[] array = path.startsWith($.SLASH_STRING) ? path.replaceFirst($.SLASH_STRING, $.EMPTY_STRING).split($.SLASH_STRING) : path.split($.SLASH_STRING);

        return $.newArrayList($.filter(this.registeredWebHandlers, new Predicate<WebHandler>() {

            @Override
            public boolean test(WebHandler webHandler)
            {
                if (((path.equals($.SLASH_STRING) || path.isEmpty()) && webHandler.getPath().equals($.SLASH_STRING)))
                    return true;

                String[] pathArray = webHandler.getPath().startsWith($.SLASH_STRING) ? webHandler.getPath().replaceFirst($.SLASH_STRING,
                        $.EMPTY_STRING).split($.SLASH_STRING) : webHandler.getPath().split($.SLASH_STRING);

                if (!webHandler.getPath().contains("*") && array.length != pathArray.length)
                    return false;

                for (short i = 0; i < array.length; i++)
                {
                    if (pathArray[i].equals("*")) return true;

                    if (!((pathArray[i].startsWith("{") && pathArray[i].endsWith("}")) || pathArray[i].equals(array[i])))
                        return false;
                }

                return true;
            }
        }));
    }

}