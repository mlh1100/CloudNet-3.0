package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.util.Properties;
import org.junit.Assert;
import org.junit.Test;

public class PropertiesTest {

    @Test
    public void testPropertiesParser()
    {
        Properties properties = $.parseLine("test=true --foo -xfy");

        Assert.assertTrue(properties.size() == 3);
        Assert.assertTrue(properties.getBoolean("test"));
    }

}