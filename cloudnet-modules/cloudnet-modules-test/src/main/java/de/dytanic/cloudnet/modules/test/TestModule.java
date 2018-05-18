package de.dytanic.cloudnet.modules.test;

import de.dytanic.cloudnet.module.Module;

import java.util.function.Consumer;

public final class TestModule extends Module {

    private static final Consumer<String> PRINT_LN = System.out::println;

    public TestModule()
    {
    }

    @Override
    public void onLoad()
    {
        PRINT_LN.accept("Hello TestModule!");
    }

    @Override
    public void onUnload()
    {
        PRINT_LN.accept("Bye TestModule!");
    }
}