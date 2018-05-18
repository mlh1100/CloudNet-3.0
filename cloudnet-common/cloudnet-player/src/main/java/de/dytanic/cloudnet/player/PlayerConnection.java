package de.dytanic.cloudnet.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created by Tareko on 27.07.2017.
 */
@Getter
@AllArgsConstructor
public class PlayerConnection {

    private UUID uniqueId;

    private String name, host;

    private int version, port;

    private boolean onlineMode, legacy;

}