package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.event.Event;
import de.dytanic.cloudnet.event.Listener;
import de.dytanic.cloudnet.event.IEventListener;
import de.dytanic.cloudnet.event.IEventManager;
import de.dytanic.cloudnet.event.impl.DefaultEventManager;
import de.dytanic.cloudnet.module.Module;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

public class EventManagerTest implements IEventListener {

    private String name;

    @Test
    public void testEventManager()
    {
        IEventManager eventManager = new DefaultEventManager();

        eventManager.registerListener(null, this);
        Assert.assertTrue(eventManager.containsListener(EventManagerTest.class));

        eventManager.callEvent(new TestEvent("TestName"));

        Assert.assertTrue(this.name != null && this.name.equals("TestName"));

        eventManager.unregisterListener((Module) null);
        Assert.assertTrue(!eventManager.containsListener(EventManagerTest.class));

        eventManager.registerListener(null, this);
        Assert.assertTrue(eventManager.containsListener(EventManagerTest.class));

        eventManager.unregisterListener(EventManagerTest.class);
        Assert.assertTrue(!eventManager.containsListener(EventManagerTest.class));

        this.name = null;

        eventManager.callEvent(new TestEvent("test"));

        Assert.assertTrue(this.name == null);
    }

    @Listener
    public void handle(TestEvent testEvent)
    {
        this.name = testEvent.getName();
    }

    @Getter
    @AllArgsConstructor
    public static final class TestEvent extends Event
    {
        private String name;
    }

}