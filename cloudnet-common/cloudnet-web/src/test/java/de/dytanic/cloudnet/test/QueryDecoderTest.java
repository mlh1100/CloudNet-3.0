package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.webserver.util.QueryDecoder;
import org.junit.Assert;
import org.junit.Test;

public class QueryDecoderTest {

    @Test
    public void testQueryDecoder()
    {
        QueryDecoder queryDecoder = null;

        try
        {
            queryDecoder = new QueryDecoder("?test=false&foo=bar&name=dytanic");
            Assert.assertTrue(true);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(queryDecoder != null);
        Assert.assertTrue(queryDecoder.getQueryParams().size() == 3);
        Assert.assertTrue(queryDecoder.getQueryParams().containsKey("test") &&
                queryDecoder.getQueryParams().containsKey("foo") &&
                queryDecoder.getQueryParams().containsKey("name"));

        Assert.assertTrue(queryDecoder.getQueryParams().get("foo").equals("bar"));


        queryDecoder = new QueryDecoder("foo=bar");
        Assert.assertTrue(queryDecoder.getQueryParams().size() == 1);
        Assert.assertTrue(queryDecoder.getQueryParams().containsKey("foo"));
        Assert.assertTrue(queryDecoder.getQueryParams().get("foo").equals("bar"));
    }

}