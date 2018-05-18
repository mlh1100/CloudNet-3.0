package de.dytanic.cloudnet.document;

import lombok.Getter;

/**
 * Created by Tareko on 02.01.2018.
 */
public class BasicDocPropertyable implements IDocPropertyable {

    @Getter
    protected Document properties = new Document();

    @Override
    public <E> IDocPropertyable setProperty(DocProperty<E> docProperty, E val)
    {
        properties.setProperty(docProperty, val);
        return this;
    }

    @Override
    public <E> E getProperty(DocProperty<E> docProperty)
    {
        return properties.getProperty(docProperty);
    }

    @Override
    public <E> IDocPropertyable removeProperty(DocProperty<E> docProperty)
    {
        properties.removeProperty(docProperty);
        return this;
    }
}