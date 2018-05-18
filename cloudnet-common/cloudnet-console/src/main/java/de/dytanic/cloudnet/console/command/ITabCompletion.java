package de.dytanic.cloudnet.console.command;

import de.dytanic.cloudnet.util.Properties;

import java.util.List;

public interface ITabCompletion {

    List<String> complete(String commandLine, String[] args, Properties properties);

}