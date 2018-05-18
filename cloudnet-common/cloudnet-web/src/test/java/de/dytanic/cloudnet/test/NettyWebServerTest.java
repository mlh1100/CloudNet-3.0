package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.util.HostAndPort;
import de.dytanic.cloudnet.webserver.IWebServer;
import de.dytanic.cloudnet.webserver.WebConfig;
import de.dytanic.cloudnet.webserver.impl.NettyWebServer;
import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.WebHandler;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NettyWebServerTest {

    @Test
    public void testNettyWebServer() throws Exception
    {
        WebConfig config = new WebConfig(false, new HostAndPort("127.0.0.1", 9932));

        NettyWebServer webServer = new NettyWebServer(config);

        Assert.assertTrue(webServer.getConfig() != null);
        Assert.assertTrue(webServer.getChannelFuture() == null);

        webServer.registerHandler(new TestWebHandler());

        Assert.assertTrue(webServer.getHandlers().size() == 1);
        Assert.assertTrue(webServer.getAvailableWebHandlers("/ping/foobar").size() == 1);

        webServer.bind();
        Assert.assertTrue(webServer.getChannelFuture() != null);

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://127.0.0.1:9932/ping/foobar").openConnection();
        httpURLConnection.setConnectTimeout(1000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.connect();

        Assert.assertTrue(httpURLConnection.getResponseCode() == 200);

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = httpURLConnection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            String input;
            while ((input = bufferedReader.readLine()) != null)
                stringBuilder.append(input);
        }

        httpURLConnection.disconnect();
        Assert.assertTrue(!stringBuilder.toString().isEmpty());

        Document document = Document.newDocument(stringBuilder.toString());
        Assert.assertTrue(document.size() == 1);

        Assert.assertTrue(document.contains("pong"));
        Assert.assertTrue(document.getString("pong").equals("foobar"));

        webServer.unregisterHandler(TestWebHandler.class);
        Assert.assertTrue(webServer.getHandlers().size() == 0);

        webServer.registerHandlers(new TestWebHandler(), new TestWebHandler());
        Assert.assertTrue(webServer.getHandlers().size() == 2);

        webServer.clearHandlers();
        Assert.assertTrue(webServer.getHandlers().size() == 0);

        webServer.close();
    }

    private final class TestWebHandler extends WebHandler {

        protected TestWebHandler()
        {
            super("ping/{value}");
        }

        @Override
        public void onHttpRequest(IWebContext webContext, PathProvider pathProvider) throws Exception
        {
            if (!webContext.getContextMethod().equalsIgnoreCase("GET") || pathProvider.getPathParameters().size() != 1)
                return;

            byte[] response = new Document("pong", pathProvider.getPathParameters().get("value")).toBytesAsUTF_8();

            webContext.setResponseContentType("application/json");

            webContext.setReponseCode(200);
            webContext.setResponseContentLength(response.length);
            webContext.setResponseData(response);
        }
    }

    /*= --------------------------------------------------------- =*/

    @Test
    public void testAliasWebHandler() throws Exception
    {

        WebConfig config = new WebConfig(false, new HostAndPort("127.0.0.1", 9933));

        IWebServer webServer = new NettyWebServer(config);
        webServer.registerHandlers(new AliasWebHandler());

        Assert.assertTrue(webServer.getHandlers().size() == 1);
        Assert.assertTrue(webServer.getHandlers().iterator().next().getClass().equals(AliasWebHandler.class));

        webServer.bind();

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://127.0.0.1:9933/test/v1/2018/caches/foobar").openConnection();
        httpURLConnection.setConnectTimeout(1000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.connect();

        Assert.assertTrue(httpURLConnection.getResponseCode() == 200);

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = httpURLConnection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            String input;
            while ((input = bufferedReader.readLine()) != null)
                stringBuilder.append(input);
        }

        httpURLConnection.disconnect();
        Assert.assertTrue(!stringBuilder.toString().isEmpty());

        Document document = Document.newDocument(stringBuilder.toString());
        Assert.assertTrue(document.size() == 2);

        Assert.assertTrue(document.getString("api_version").equals("v1"));
        Assert.assertTrue(document.getStrings("aliases")[1].equals("caches"));

        webServer.close();
    }

    private final class AliasWebHandler extends WebHandler {

        public AliasWebHandler()
        {
            super("test/{api_version}/*");
        }

        @Override
        public void onHttpRequest(IWebContext webContext, PathProvider pathProvider) throws Exception
        {
            if (!webContext.getContextMethod().equalsIgnoreCase("GET") || pathProvider.getPathParameters().size() != 1 ||
                    pathProvider.getPathAliases().length != 3) return;

            byte[] response = new Document("api_version", pathProvider.getPathParameters().get("api_version"))
                    .append("aliases", pathProvider.getPathAliases()).toBytesAsUTF_8();

            webContext.setResponseContentType("application/json");

            webContext.setReponseCode(200);
            webContext.setResponseContentLength(response.length);
            webContext.setResponseData(response);
        }
    }

}