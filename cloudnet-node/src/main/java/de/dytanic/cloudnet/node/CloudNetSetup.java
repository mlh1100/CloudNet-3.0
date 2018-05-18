package de.dytanic.cloudnet.node;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.console.animation.CAQuestionList;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.language.LanguageManager;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.util.Properties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CloudNetSetup {

    private IConsoleProvider console;

    private ILogProvider logger;

    private Properties properties;

    private Document out;

    private SetupType type;

    static void createSetup(Properties properties, ILogProvider logger, IConsoleProvider console, Document out) throws Exception
    {
        CloudNetSetup setup = new CloudNetSetup(console, logger, properties, out, null);
        console.clearScreen();
        printHeader(setup);

        //-
        preConfiguration(setup);

        if (setup.type.equals(SetupType.DEFAULT))
        {
            setup.properties.put("setup-host", "0.0.0.0");
            setup.properties.put("setup-port", "1410");
            setup.properties.put("setup-web-host", "0.0.0.0");
            setup.properties.put("setup-web-port", "2812");
            setup.properties.put("setup-web-ssl", "false");
            //-
            setup.properties.put("setup-start-service-cpu-limiter", "70");
            setup.properties.put("setup-memory-limiter", "2048");
        }


        setupConfiguration(setup);

        console.clearScreen();
    }

    //-

    private static void preConfiguration(CloudNetSetup setup)
    {
        CAQuestionList questionList = new CAQuestionList(
                false,
                true,
                1000,
                ">&4"
        );

        questionList.add(new CAQuestionList.QuestionListEntry(
                "type",
                LanguageManager.getMessage("setup-config-default_or_manual"),
                LanguageManager.getMessage("setup-config-default_or_manual-invalid-input"),
                CAQuestionList.QuestionResponseType.STRING,
                new Predicate<String>() {
                    @Override
                    public boolean test(String s)
                    {
                        return s.toLowerCase().equals("manual") || s.toLowerCase().equals("default");
                    }
                },
                Arrays.asList("manual", "default")
        ));

        setup.console.invokeConsoleAnimation(questionList);
        setup.type = SetupType.valueOf(questionList.getResponse().getString("type").toUpperCase());
    }

    private static void setupConfiguration(CloudNetSetup setup)
    {
        CAQuestionList caQuestionList = new CAQuestionList(
                false,
                false,
                1000,
                ">&4"
        );

        if (!setup.properties.containsKey("setup-host"))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "node-host",
                    LanguageManager.getMessage("setup-server-host"),
                    "invalid node hostname",
                    CAQuestionList.QuestionResponseType.STRING,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            return true;
                        }
                    },
                    Arrays.asList("localhost", "127.0.0.1", "0.0.0.0")
            ));
        else setup.out.append("node-host", setup.properties.get("setup-host"));

        if (!setup.properties.containsKey("setup-port") || !$.isNumber(setup.properties.get("setup-port")))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "node-port",
                    LanguageManager.getMessage("setup-server-port"),
                    LanguageManager.getMessage("setup-port-invalid"),
                    CAQuestionList.QuestionResponseType.STRING,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            try
                            {
                                return Integer.parseInt(s) <= 65535;
                            } catch (NumberFormatException ex)
                            {
                                return false;
                            }
                        }
                    },
                    Arrays.asList("1410", "2812")
            ));
        else setup.out.append("node-port", Integer.parseInt(setup.properties.get("setup-port")));

        if (!setup.properties.containsKey("setup-web-host"))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "node-web-host",
                    LanguageManager.getMessage("setup-web-server-host"),
                    "invalid node web hostname",
                    CAQuestionList.QuestionResponseType.STRING,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            return true;
                        }
                    },
                    Arrays.asList("0.0.0.0")
            ));
        else setup.out.append("node-web-host", setup.properties.get("setup-web-host"));

        if (!setup.properties.containsKey("setup-web-port") || !$.isNumber(setup.properties.get("setup-web-port")))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "node-web-port",
                    LanguageManager.getMessage("setup-web-server-port"),
                    LanguageManager.getMessage("setup-port-invalid"),
                    CAQuestionList.QuestionResponseType.STRING,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            try
                            {
                                return Integer.parseInt(s) <= 65535;
                            } catch (NumberFormatException ex)
                            {
                                return false;
                            }
                        }
                    },
                    Arrays.asList("1410", "2812")
            ));
        else setup.out.append("node-web-port", Integer.parseInt(setup.properties.get("setup-web-port")));

        if (!setup.properties.containsKey("setup-web-ssl"))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "node-web-ssl",
                    LanguageManager.getMessage("setup-web-server-ssl"),
                    LanguageManager.getMessage("setup-web-server-ssl-invalid-input"),
                    CAQuestionList.QuestionResponseType.STRING,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            return s.toLowerCase().equals("true") || s.toLowerCase().equals("false");
                        }
                    },
                    Arrays.asList("true", "false")
            ));
        else setup.out.append("node-web-ssl", Boolean.parseBoolean(setup.properties.get("setup-web-ssl")));

        if (!setup.properties.containsKey("setup-start-service-cpu-limiter"))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "start-service-cpu-limiter",
                    LanguageManager.getMessage("setup-service-cpu-limiter"),
                    LanguageManager.getMessage("setup-number-invalid"),
                    CAQuestionList.QuestionResponseType.NUMBER,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            try
                            {
                                return Integer.parseInt(s) < 100;
                            } catch (NumberFormatException ex)
                            {
                                return false;
                            }
                        }
                    },
                    Arrays.asList("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100")
            ));
        else
            setup.out.append("start-service-cpu-limiter", Integer.parseInt(setup.properties.get("setup-start-service-cpu-limiter")));

        if (!setup.properties.containsKey("setup-memory-limiter"))
            caQuestionList.add(new CAQuestionList.QuestionListEntry(
                    "memory-limiter",
                    LanguageManager.getMessage("setup-memory-limiter"),
                    LanguageManager.getMessage("setup-number-invalid"),
                    CAQuestionList.QuestionResponseType.NUMBER,
                    new Predicate<String>() {
                        @Override
                        public boolean test(String s)
                        {
                            try
                            {
                                Integer.parseInt(s);
                                return true;
                            } catch (NumberFormatException ex)
                            {
                                return false;
                            }
                        }
                    },
                    Arrays.asList("1024", "2048", "3072", "4096", "6144", "8192", "16384", "32768", "65536")
            ));
        else setup.out.append("memory-limiter", Integer.parseInt(setup.properties.get("setup-memory-limiter")));

        setup.console.invokeConsoleAnimation(caQuestionList);

        setup.out.append(caQuestionList.getResponse());
    }

    private static void printHeader(CloudNetSetup setup)
    {
        setup.console.writeLine(
                "\n     _______. _______ .___________. __    __  .______   \n" +
                        "    /       ||   ____||           ||  |  |  | |   _  \\  \n" +
                        "   |   (----`|  |__   `---|  |----`|  |  |  | |  |_)  | \n" +
                        "    \\   \\    |   __|      |  |     |  |  |  | |   ___/  \n" +
                        ".----)   |   |  |____     |  |     |  `--'  | |  |      \n" +
                        "|_______/    |_______|    |__|      \\______/  | _|      \n" +
                        "                                                        \n");
    }

    enum SetupType {
        MANUAL,
        DEFAULT
    }
}