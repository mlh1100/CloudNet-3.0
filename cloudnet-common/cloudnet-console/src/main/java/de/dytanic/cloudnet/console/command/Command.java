package de.dytanic.cloudnet.console.command;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.document.Document;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class Command extends BasicDocPropertyable implements ICommandExecutor {

    protected String[] names;

    protected String permission;

    protected String description;

    protected String usage;

    protected String prefix;

    public Command(String... names)
    {
        this.names = names;
    }

    public Command(String[] names, String permission)
    {
        this.names = names;
        this.permission = permission;
    }

    public Command(String[] names, String permission, String description)
    {
        this.names = names;
        this.permission = permission;
        this.description = description;
    }

    public Command(String[] names, String permission, String description, String usage, String prefix)
    {
        this.names = names;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.prefix = prefix;
    }

    public CommandInfo getInfo()
    {
        return new CommandInfo(this.names, permission, description, usage, properties);
    }

    public final boolean isValid()
    {
        return this.names != null && this.names.length > 0;
    }

    public final Document getProperties()
    {
        return properties;
    }

}