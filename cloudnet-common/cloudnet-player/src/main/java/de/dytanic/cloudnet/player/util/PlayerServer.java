package de.dytanic.cloudnet.player.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerServer {

    protected UUID server;

    protected long timeStamp;

}