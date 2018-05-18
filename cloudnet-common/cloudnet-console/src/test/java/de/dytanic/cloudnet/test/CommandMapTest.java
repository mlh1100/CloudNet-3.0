package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.console.command.Command;
import de.dytanic.cloudnet.console.command.ICommandMap;
import de.dytanic.cloudnet.console.command.ICommandSender;
import de.dytanic.cloudnet.console.impl.DefaultCommandMap;
import de.dytanic.cloudnet.console.impl.JAnsiConsoleProvider;
import de.dytanic.cloudnet.util.Properties;
import org.junit.Assert;
import org.junit.Test;

public final class CommandMapTest {

    private String name;

    private String b;

    @Test
    public void testMap()
    {
        ICommandMap commandMap = new DefaultCommandMap();

        ICommandSender commandSender = new ICommandSender() {

            @Override
            public void sendMessage(String message)
            {
                b = message;
            }

            @Override
            public void sendMessage(String... messages)
            {

            }

            @Override
            public boolean hasPermission(String permission)
            {
                return true;
            }

            @Override
            public String getName()
            {
                return "test";
            }
        };

        final Command command = new Command() {

            {
                names = new String[]{"test"};
                description = "description";
                prefix = "test";
            }

            @Override
            public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties)
            {
                Assert.assertTrue(args.length == 2);
                Assert.assertTrue(args[1].equals("val"));

                CommandMapTest.this.name = args[0];

                sender.sendMessage("Hello, world!");
            }
        };

        commandMap.registerCommand(command);

        Assert.assertTrue(commandMap.getCommand("TEST") != null);
        Assert.assertTrue(commandMap.getCommand("test:test") != null);
        Assert.assertTrue(commandMap.getCommandNames().size() == 2);
        Assert.assertTrue(commandMap.dispatchCommand(commandSender, "test:test Dytanic val"));

        commandMap.unregisterCommand(command.getClass());
        Assert.assertTrue(commandMap.getCommandNames().size() == 0);

        Assert.assertTrue(this.name != null);
        Assert.assertTrue(this.name.equals("Dytanic"));
        Assert.assertTrue(b.equals("Hello, world!"));
    }

}