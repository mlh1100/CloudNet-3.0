package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.registry.DefaultServiceRegistry;
import de.dytanic.cloudnet.registry.IRegistryService;
import de.dytanic.cloudnet.registry.IServiceProvider;
import de.dytanic.cloudnet.registry.IServiceRegistry;
import org.junit.Assert;
import org.junit.Test;

public class ServiceRegistryTest implements IServiceProvider {

    @Test
    public void testRegistry()
    {
        IServiceRegistry serviceRegistry = new DefaultServiceRegistry();

        serviceRegistry.registerService(TestService.class, this, new TestServiceImpl());

        Assert.assertTrue(serviceRegistry.getService(TestService.class, "test") != null);
        Assert.assertTrue(serviceRegistry.getService(TestService.class, "test").getData().equals("foo"));

        serviceRegistry.removeService(TestService.class, "test");
        Assert.assertTrue(!serviceRegistry.hasService(TestService.class, "test"));

        serviceRegistry.registerService(TestService.class, this, new TestServiceImpl());

        serviceRegistry.removeServices(this);
        Assert.assertTrue(!serviceRegistry.hasService(TestService.class, "test"));
    }

    private abstract class TestService implements IRegistryService {

        public abstract String getData();

    }

    private class TestServiceImpl extends TestService {

        @Override
        public String getData()
        {
            return "foo";
        }

        @Override
        public String getServiceName()
        {
            return "test";
        }
    }

}