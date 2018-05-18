package de.dytanic.cloudnet.player;

import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.player.util.ChatHistoryEntry;
import de.dytanic.cloudnet.player.util.PlayerServer;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Data
public final class CloudPlayer extends CloudOfflinePlayer {

    public static final Type TYPE = new TypeToken<CloudPlayer>() {
    }.getType();

    protected PlayerServer currentProxy, currentServer;

    protected PlayerConnection playerConnection;

    public CloudPlayer()
    {
    }

}