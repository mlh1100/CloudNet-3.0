package de.dytanic.cloudnet.webserver.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.util.HostAndPort;
import de.dytanic.cloudnet.webserver.util.QueryDecoder;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public final class NettyWebContext implements IWebContext {

    private final String uri;

    private final ChannelHandlerContext channelHandlerContext;

    private final HttpRequest httpRequest;

    private final QueryDecoder queryParameters;

    private final DefaultFullHttpResponse defaultFullHttpResponse;

    private byte[] data;

    private String path;

    public NettyWebContext(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest)
    {
        this.uri = httpRequest.uri();
        this.channelHandlerContext = channelHandlerContext;
        this.httpRequest = httpRequest;

        URI uri = getURIInstance();

        this.path = uri.getPath();
        this.queryParameters = new QueryDecoder(uri.getQuery());
        this.defaultFullHttpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.valueOf(404), Unpooled.buffer());
    }

    @Override
    public URI getURIInstance()
    {
        try
        {
            return new URI(uri);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getContextMethod()
    {
        return httpRequest.method().name();
    }

    @Override
    public String getProtcolVersion()
    {
        return httpRequest.protocolVersion().protocolName();
    }

    @Override
    public HostAndPort getClientAddress()
    {
        if (this.channelHandlerContext.channel().remoteAddress() instanceof InetSocketAddress)
        {
            InetSocketAddress socketAddress = (InetSocketAddress) this.channelHandlerContext.channel().remoteAddress();
            return new HostAndPort(socketAddress.getHostString(), socketAddress.getPort());
        } else return null;
    }

    @Override
    public String getRequestHeaderProperty(String key)
    {
        return this.httpRequest.headers().get(key);
    }

    @Override
    public boolean hasRequestHeaderProperty(String key)
    {
        return this.httpRequest.headers().contains(key);
    }

    @Override
    public IWebContext setResponseHeader(String key, String value)
    {
        this.defaultFullHttpResponse.headers().set(key, value);
        return this;
    }

    @Override
    public String getResponseHeaderProperty(String key)
    {
        return this.defaultFullHttpResponse.headers().get(key);
    }

    @Override
    public boolean hasResponseHeaderProperty(String key)
    {
        return this.defaultFullHttpResponse.headers().contains(key);
    }

    @Override
    public Map<String, String> getResponseHeaders(String key)
    {
        Map<String, String> map = $.newHashMap();

        this.defaultFullHttpResponse.headers().forEach(new Consumer<Map.Entry<String, String>>() {
            @Override
            public void accept(Map.Entry<String, String> stringStringEntry)
            {
                map.put(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        });

        return map;
    }

    @Override
    public IWebContext setReponseCode(int code)
    {
        this.defaultFullHttpResponse.setStatus(HttpResponseStatus.valueOf(code));
        return this;
    }

    @Override
    public IWebContext setResponseData(byte[] data)
    {
        this.data = data;
        return this;
    }

    @Override
    public boolean hasResponse()
    {
        return data != null;
    }

    @Override
    public byte[] getResponseData()
    {
        return data;
    }

    @Override
    public int getResponseCode()
    {
        return this.defaultFullHttpResponse.status().code();
    }

    @Override
    public void close() throws Exception
    {
        if (this.data != null)
            this.defaultFullHttpResponse.content().writeBytes(this.data);
    }

    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
    }
}