package de.dytanic.cloudnet.module.exception;

/**
 * Throws if a ModuleConfig cannot be found in a .jar, .war or .zip Archive
 */
public class ModuleConfigNotFoundException extends Exception {

    public ModuleConfigNotFoundException()
    {
    }

    public ModuleConfigNotFoundException(String message)
    {
        super(message);
    }
}