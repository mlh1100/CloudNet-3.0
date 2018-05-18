package de.dytanic.cloudnet.console.impl.completer;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.command.Command;
import de.dytanic.cloudnet.console.command.ICommandMap;
import de.dytanic.cloudnet.console.command.ITabCompletion;
import jline.console.completer.Completer;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public final class CommandCompleter implements Completer {

    private final ICommandMap commandMap;

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates)
    {
        List<String> responses = $.newArrayList();

        if (buffer.isEmpty() || buffer.indexOf(' ') == -1)
        {
            responses.addAll($.filter(commandMap.getCommandNames(), new Predicate<String>() {

                @Override
                public boolean test(String s)
                {
                    return s != null && s.contains(buffer.toLowerCase());
                }
            }));
        } else
        {
            Command command = commandMap.getCommandFromLine(buffer);

            if (command != null && command instanceof ITabCompletion)
            {
                String[] args = buffer.split($.SPACE_STRING);
                String testString = args[args.length - 1];
                args = buffer.replaceFirst(args[0] + $.SPACE_STRING, "").split($.SPACE_STRING);

                responses.addAll($.filter(((ITabCompletion) command).complete(buffer, args, $.parseLine(args)), new Predicate<String>() {

                    @Override
                    public boolean test(String s)
                    {
                        return s != null && (testString.isEmpty() || s.toLowerCase().contains(testString.toLowerCase()));
                    }
                }));
            }
        }

        candidates.addAll(responses);
        int lastSpace = buffer.lastIndexOf(' ');

        return (lastSpace == -1) ? cursor - buffer.length() : cursor - (buffer.length() - lastSpace - 1);
    }
}