package de.dytanic.cloudnet.event.interfaces;

/**
 * Created by Tareko on 20.12.2017.
 */
public interface Cancelable {

    boolean isCancelled();

    void setCancelled(boolean val);

}