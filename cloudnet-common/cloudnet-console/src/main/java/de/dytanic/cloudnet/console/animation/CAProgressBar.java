package de.dytanic.cloudnet.console.animation;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.console.IConsoleProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.fusesource.jansi.Ansi;

import java.text.SimpleDateFormat;

/**
 * The ProgressBar represents a progressing situation of a program part.
 * For example
 * <p>
 * CAProgressBar progressBar = new CAProgressBar(
 * '█',
 * "[%percent%%] ",
 * " | %value%/%target%MB Downloaded... (%time%)",
 * 3,
 * 2048,
 * false
 * );
 * <p>
 * TaskScheduler.runtimeScheduler().schedule(new Runnable() {
 *
 * @Override public void run()
 * {
 * if (progressBar.getProgressValue() < progressBar.getTargetGoal())
 * progressBar.setProgressValue(progressBar.getProgressValue() + 1);
 * }
 * }, 5, -1);
 * consoleProvider.invokeConsoleAnimation(progressBar);
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class CAProgressBar extends AbstractConsoleAnimation {

    protected long updateInterval, targetGoal, barStart;

    protected boolean expand;

    protected volatile long progressValue;

    protected String prefix, suffix;

    protected char progressChar;

    public CAProgressBar(char progressChar, String prefix, String suffix, long updateInterval, long targetGoal, boolean doExpand)
    {
        this.progressChar = progressChar;
        this.prefix = prefix;
        this.suffix = suffix;
        this.updateInterval = updateInterval;
        this.targetGoal = targetGoal;
        this.expand = doExpand;
    }

    @Override
    public void start(IConsoleProvider consoleProvider)
    {
        execute(consoleProvider);

        this.barStart = System.currentTimeMillis();
        while (progressValue < targetGoal)
        {
            execute(consoleProvider);
            $.sleepUninterruptedly(updateInterval);
        }

        execute(consoleProvider);
    }

    protected void execute(IConsoleProvider consoleProvider)
    {
        int percent = (int) ((progressValue * 100) / targetGoal);
        StringBuilder stringBuilder = new StringBuilder("");

        if (expand)
        {
            for (int i = 0; i < percent; i++)
                if (i % 2 == 0) stringBuilder.append(progressChar);
        } else
            for (int i = 0; i < 100; i++)
                if (i % 2 == 0) stringBuilder.append(i < percent ? progressChar : $.SPACE_STRING);

        consoleProvider.write(
                Ansi
                        .ansi()
                        .saveCursorPosition()
                        .cursorUp(1)
                        .eraseLine(Ansi.Erase.ALL)
                        .a(insertPatterns(prefix, percent))
                        .a(stringBuilder.toString())
                        .a(insertPatterns(suffix, percent))
                        .restoreCursorPosition()
                        .toString()
        );
    }

    protected String insertPatterns(String value, int percent)
    {
        return value
                .replace("%percent%", percent + $.EMPTY_STRING)
                .replace("%time%", new SimpleDateFormat("mm:ss").format(System.currentTimeMillis() - barStart))
                .replace("%target%", targetGoal + $.EMPTY_STRING)
                .replace("%value%", this.progressValue + $.EMPTY_STRING);
    }

}