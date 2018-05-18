package de.dytanic.cloudnet.module.exception;

import java.io.IOException;

/**
 * Throws if a module cannot be successfully load
 */
public class ModuleLoadException extends IOException {

    public ModuleLoadException()
    {
    }

    public ModuleLoadException(String message)
    {
        super(message);
    }
}