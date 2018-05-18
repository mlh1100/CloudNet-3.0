package de.dytanic.cloudnet.console.animation;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.ConsoleColour;
import de.dytanic.cloudnet.console.IConsoleProvider;
import de.dytanic.cloudnet.document.Document;
import jline.console.history.History;
import jline.console.history.MemoryHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fusesource.jansi.Ansi;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;

public final class CAQuestionList extends AbstractConsoleAnimation {

    private final Map<String, QuestionListEntry> entries = $.newLinkedMap();

    @Getter
    private final Document response = new Document();

    private final boolean clearScreenBefore, usePromptForAsk;

    private final long invalidMessageShowMillis;

    private final String answerPromptSuffix;

    public CAQuestionList(boolean clearScreenBefore, boolean usePromptForAsk, long invalidMessageShowMillis)
    {
        this(clearScreenBefore, usePromptForAsk, invalidMessageShowMillis, null);
    }

    public CAQuestionList(boolean clearScreenBefore, boolean usePromptForAsk, long invalidMessageShowMillis, String answerPromptSuffix)
    {
        this.clearScreenBefore = clearScreenBefore;
        this.usePromptForAsk = usePromptForAsk;
        this.invalidMessageShowMillis = invalidMessageShowMillis;
        this.answerPromptSuffix = answerPromptSuffix == null ? "> " : answerPromptSuffix;
    }

    public CAQuestionList add(QuestionListEntry... entries)
    {
        if (entries == null) return this;

        for (QuestionListEntry questionListEntry : entries)
            if (questionListEntry != null && questionListEntry.name != null && questionListEntry.question != null &&
                    questionListEntry.responseType != null && questionListEntry.validationPredicate != null &&
                    questionListEntry.invalidArgument != null && !this.entries.containsKey(questionListEntry.name))
                this.entries.put(questionListEntry.name, questionListEntry);

        return this;
    }

    /*= ----------------------------------------------------- =*/

    @Override
    public void start(IConsoleProvider consoleProvider)
    {
        if(this.entries.size() == 0) return;

        History oldHistory = consoleProvider.getConsoleReader().getHistory();
        QuestionListEntry entry = null;
        Queue<QuestionListEntry> cloned = new ArrayBlockingQueue<>(this.entries.size(), true, this.entries.values());

        if (clearScreenBefore) consoleProvider.clearScreen();

        while ((entry = cloned.poll()) != null)
        {
            consoleProvider.getConsoleReader().setHistory(buildHistory(entry));

            if (!this.usePromptForAsk) consoleProvider.writeLine(entry.question);

            while (!this.response.contains(entry.name))
            {
                try
                {

                    String input = consoleProvider.getConsoleReader().readLine(
                            translate(this.usePromptForAsk ? entry.question + $.SPACE_STRING + this.answerPromptSuffix + $.SPACE_STRING :
                                    this.answerPromptSuffix + $.SPACE_STRING));

                    consoleProvider.getConsoleReader().setPrompt("");

                    if ((entry.responseType.equals(QuestionResponseType.NUMBER) && !$.isNumber(input)))
                    {
                        invalid(consoleProvider, entry);
                        continue;
                    }

                    if (!entry.validationPredicate.test(input))
                    {
                        invalid(consoleProvider, entry);
                        continue;
                    }

                    this.response.append(entry.name, entry.responseType.equals(QuestionResponseType.NUMBER) ? Double.parseDouble(input) : input);

                    if (!usePromptForAsk)
                    {
                        consoleProvider.write(
                                Ansi.ansi().reset().cursorUp(1).cursorLeft(Integer.MAX_VALUE)
                                        .eraseLine()
                                        .cursorUp(1).eraseLine()
                                        .a(entry.question).a($.SPACE_STRING).a(this.answerPromptSuffix).a($.SPACE_STRING)
                                        .a(input).a(System.lineSeparator()).toString()
                        );
                    }

                    consoleProvider.writeLine();

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        consoleProvider.getConsoleReader().setHistory(oldHistory);
    }

    /*= ----------------------------------------------------- =*/

    private void invalid(IConsoleProvider consoleProvider, QuestionListEntry entry)
    {
        consoleProvider.write(
                Ansi.ansi().reset().eraseLine().cursorUp(1).eraseLine().toString() +
                        translate(entry.invalidArgument) +
                        Ansi.ansi().cursorDown(1).cursorLeft(Integer.MAX_VALUE).toString()
        );

        $.sleepUninterruptedly(invalidMessageShowMillis);

        consoleProvider.write(
                Ansi.ansi().reset().cursorUp(1).eraseLine().toString()
        );

        consoleProvider.getConsoleReader().setHistory(buildHistory(entry));
    }

    private History buildHistory(QuestionListEntry entry)
    {
        History history = new MemoryHistory();

        if (entry.autoCompletedAnswers == null) return history;

        for (String answer : entry.autoCompletedAnswers)
            if (answer != null)
                history.add(answer);

        return history;
    }

    private String translate(String input)
    {
        return input == null ? null : ConsoleColour.toColouredString('&', ConsoleColour.toColouredString('§', input));
    }

    /*= ----------------------------------------------------- =*/

    @Getter
    @AllArgsConstructor
    public static class QuestionListEntry {

        private final String name, question, invalidArgument;

        private final QuestionResponseType responseType;

        private final Predicate<String> validationPredicate;

        private final Collection<String> autoCompletedAnswers;

    }

    public enum QuestionResponseType {
        NUMBER,
        STRING
    }

}