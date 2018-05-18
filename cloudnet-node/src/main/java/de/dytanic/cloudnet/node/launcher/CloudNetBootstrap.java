package de.dytanic.cloudnet.node.launcher;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.ConsoleColour;
import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.console.impl.DefaultCommandMap;
import de.dytanic.cloudnet.console.impl.JAnsiConsoleProvider;
import de.dytanic.cloudnet.console.log.ConsoleLogHandler;
import de.dytanic.cloudnet.core.event.command.CommandPostProcessEvent;
import de.dytanic.cloudnet.core.event.command.CommandPreProcessEvent;
import de.dytanic.cloudnet.core.event.console.ConsoleCommandReadPromptFormatEvent;
import de.dytanic.cloudnet.core.log.DefaultLogFormatter;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.language.LanguageManager;
import de.dytanic.cloudnet.logging.ILogHandler;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.logging.LogLevel;
import de.dytanic.cloudnet.logging.impl.AsyncLogProvider;
import de.dytanic.cloudnet.logging.impl.ErrorFileLogHandler;
import de.dytanic.cloudnet.logging.impl.FileLogHandler;
import de.dytanic.cloudnet.logging.util.AbstractLogHandler;
import de.dytanic.cloudnet.logging.util.AsyncPrintStream;
import de.dytanic.cloudnet.logging.util.IFormatter;
import de.dytanic.cloudnet.logging.util.LogOutputStream;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.node.CloudNet;
import de.dytanic.cloudnet.util.Properties;

import java.io.File;

final class CloudNetBootstrap {

    synchronized static void main(String... args) throws Throwable
    {
        Properties properties = $.parseLine(args);
        properties.toLower();

        if (!setupProperties(properties)) return;

        IConsoleProvider consoleProvider = new JAnsiConsoleProvider();

        ILogProvider logProvider = new AsyncLogProvider()
                .addLogHandler(new FileLogHandler(new File(properties.getOrDefault("log-directory", "local/logs")),
                        "history", FileLogHandler.SIZE_16MB))
                .addLogHandler(new ErrorFileLogHandler(properties.getOrDefault("error-log-directory", "local/logs/error")))
                .addLogHandler(new ConsoleLogHandler(consoleProvider));

        IFormatter formatter = new DefaultLogFormatter();

        for (ILogHandler logHandler : logProvider.getLogHandlers())
            if (logHandler instanceof AbstractLogHandler)
                ((AbstractLogHandler) logHandler).setFormatter(formatter);

        System.setOut(new AsyncPrintStream(new LogOutputStream(logProvider, LogLevel.INFO)));
        System.setErr(new AsyncPrintStream(new LogOutputStream(logProvider, LogLevel.ERROR)));

        LanguageManager.addLanguageFile("german", CloudNetBootstrap.class.getClassLoader().getResourceAsStream("lang/german.properties"));

        if (LanguageManager.getLanguage() == null)
            LanguageManager.setLanguage("german");

        CloudNet cloudNet = new CloudNet(args, properties, logProvider, consoleProvider, new DefaultCommandMap(consoleProvider));
        CloudNet.setInstance(cloudNet);

        CloudNetDriver.setInstance(cloudNet);

        cloudNet.setup();
        displayHeader(logProvider);

        cloudNet.start();

        Runtime.getRuntime().addShutdownHook(new Thread(cloudNet::close, "CloudNet-Shutdown-Thread"));

        if (!properties.containsKey("noconsole"))
            runConsole(cloudNet, consoleProvider, logProvider);
        else
            while (!Thread.currentThread().isInterrupted())
                try
                {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException ex)
                {
                }
    }

    private static void displayHeader(ILogProvider logProvider)
    {
        logProvider.info(
                "   ______    __                      __    _   __         __           _____      ____ ",
                "  / ____/   / /  ____   __  __  ____/ /   / | / /  ___   / /_         |__  /     / __ \\",
                " / /       / /  / __ \\ / / / / / __  /   /  |/ /  / _ \\ / __/          /_ <     / / / /",
                "/ /___    / /  / /_/ // /_/ / / /_/ /   / /|  /  /  __// /_          ___/ /  _ / /_/ / ",
                "\\____/   /_/   \\____/ \\__,_/  \\__,_/   /_/ |_/   \\___/ \\__/         /____/  (_)\\____/  ",
                " ",
                "«*» CloudNet-Node " + CloudNetBootstrap.class.getPackage().getImplementationVersion() + " " + CloudNetBootstrap.class.getPackage().getSpecificationVersion(),
                "«*» Discord Support - https://discord.gg/CPCWr7w",
                " "
        );
    }

    private static boolean setupProperties(Properties properties)
    {
        if (properties.containsKey("version"))
        {
            System.out.println(
                    "CloudNet " + CloudNetBootstrap.class.getPackage().getSpecificationVersion() +
                            " - " + CloudNetBootstrap.class.getPackage().getImplementationVersion() + " by Dytanic"
            );
            return false;
        }

        if (properties.containsKey("lang") && !properties.get("lang").equals("true"))
            LanguageManager.setLanguage(properties.get("lang"));

        if (!properties.containsKey("module-dir"))
            properties.put("module-dir", IModuleManager.DEFAULT_MODULE_DIRECTORY);

        return true;
    }

    private static void runConsole(CloudNet cloudNet, IConsoleProvider consoleProvider, ILogProvider logProvider)
    {

        while (CloudNet.RUNNING)
        {
            try
            {
                String input;

                consoleProvider.getConsoleReader().setPrompt($.EMPTY_STRING);
                while (CloudNet.RUNNING && (input = consoleProvider.getConsoleReader().readLine(setPromptFormat(cloudNet, consoleProvider))) != null)
                {
                    consoleProvider.getConsoleReader().setPrompt($.EMPTY_STRING);

                    CommandPreProcessEvent commandPreProcessEvent = new CommandPreProcessEvent(input, cloudNet.getConsoleCommandSender(), false);
                    cloudNet.getEventManager().callEvent(commandPreProcessEvent);

                    if (!commandPreProcessEvent.isCancelled())
                        if (!cloudNet.getCommandMap().dispatchCommand(CloudNet.getInstance().getConsoleCommandSender(), input))
                            logProvider.info(LanguageManager.getMessage("console-command-not-found"));
                        else
                            cloudNet.getEventManager().callEvent(new CommandPostProcessEvent(cloudNet.getConsoleCommandSender(), input));
                }

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private static String setPromptFormat(CloudNet cloudNet, IConsoleProvider consoleProvider)
    {
        ConsoleCommandReadPromptFormatEvent consolePromptFormatEvent = new ConsoleCommandReadPromptFormatEvent(
                ConsoleColour.RED + $.SYS_PROPERTY_USER + ConsoleColour.DEFAULT + "@" + ConsoleColour.WHITE + consoleProvider.getConsoleChannelName() + ConsoleColour.DEFAULT + ": $ "
        );

        cloudNet.getEventManager().callEvent(consolePromptFormatEvent);

        return consolePromptFormatEvent.getPrompt() == null ? ">" : consolePromptFormatEvent.getPrompt();
    }

}