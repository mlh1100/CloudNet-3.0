package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.document.DocProperty;
import de.dytanic.cloudnet.document.Document;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DocumentTest {

    @Test
    public void testDocument()
    {
        Document document = new Document();

        Assert.assertTrue(document.append("foo", "bar") != null);

        document.append("number", 4).append("test", new TestClass("myData"));

        Assert.assertTrue(document != null);
        Assert.assertTrue(document.getString("foo").equals("bar"));
        Assert.assertTrue(document.getInt("number") == 4);
        Assert.assertTrue(document.getObject("test", TestClass.class).data.equals("myData"));

    }

    @Test
    public void testProperties()
    {
        DocProperty<Pair<String, String>> docProperty = new DocProperty<>(

                new BiConsumer<Pair<String, String>, Document>() {
                    @Override
                    public void accept(Pair<String, String> stringStringPair, Document document)
                    {
                        document
                                .append("firstProp", stringStringPair.getKey())
                                .append("secondProp", stringStringPair.getValue());
                    }
                },
                new Function<Document, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> apply(Document document)
                    {
                        if(!document.contains("firstProp") || !document.contains("secondProp")) return null;

                        return new Pair<>(document.getString("firstProp"), document.getString("secondProp"));
                    }
                },
                new Consumer<Document>() {
                    @Override
                    public void accept(Document document)
                    {
                        document.remove("firstProp");
                        document.remove("secondProp");
                    }
                }
        );

        Document document = new Document();
        document.setProperty(docProperty, new Pair<>("foo", "bar"));

        Assert.assertTrue(document.hasProperty(docProperty));
        Assert.assertTrue(document.getProperty(docProperty).getKey().equals("foo"));
        Assert.assertTrue(document.getProperty(docProperty).getValue().equals("bar"));

        document.removeProperty(docProperty);

        Assert.assertTrue(document.size() == 0);
    }

    private class TestClass {
        private String data;

        public TestClass(String data)
        {
            this.data = data;
        }

        public String getData()
        {
            return data;
        }
    }

}