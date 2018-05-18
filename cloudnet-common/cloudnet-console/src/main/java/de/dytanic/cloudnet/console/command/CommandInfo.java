package de.dytanic.cloudnet.console.command;

import de.dytanic.cloudnet.document.Document;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CommandInfo {

    protected String[] names;

    protected String permission;

    protected String description;

    protected String usage;

    protected Document properties;

}