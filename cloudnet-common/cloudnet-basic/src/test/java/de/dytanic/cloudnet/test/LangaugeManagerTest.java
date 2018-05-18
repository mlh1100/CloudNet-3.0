package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.language.LanguageManager;
import de.dytanic.cloudnet.util.Properties;
import org.junit.Assert;
import org.junit.Test;

public class LangaugeManagerTest {

    @Test
    public void test()
    {
        Properties properties = new Properties();
        properties.put("test_message", "Test_Message");

        LanguageManager.setLanguage("en");
        LanguageManager.addLanguageFile("en", properties);

        Assert.assertTrue(LanguageManager.getMessage("test_message") != null);
        Assert.assertTrue(LanguageManager.getMessage("test_message").equals("Test_Message"));
    }

}