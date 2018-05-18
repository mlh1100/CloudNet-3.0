package de.dytanic.cloudnet.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Val<T> {

    protected T value;

    public synchronized void saveSet(T value)
    {
        this.value = value;
    }

}