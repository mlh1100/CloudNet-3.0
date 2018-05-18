package de.dytanic.cloudnet.logging.util;

import de.dytanic.cloudnet.logging.ILogHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractLogHandler implements ILogHandler {

    @Getter
    protected IFormatter formatter = IFormatter.defaultFormatter();

    public AbstractLogHandler setFormatter(IFormatter formatter)
    {
        this.formatter = formatter;
        return this;
    }

}