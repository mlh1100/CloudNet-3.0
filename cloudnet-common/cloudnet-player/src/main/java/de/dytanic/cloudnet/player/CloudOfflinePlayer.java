package de.dytanic.cloudnet.player;

import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.player.util.ChatHistoryEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudOfflinePlayer extends BasicDocPropertyable {

    public static final Type TYPE = new TypeToken<CloudOfflinePlayer>(){}.getType();

    private UUID uniqueId;

    private String playerName;

    private long firstLogin, lastLogin, onlineTime, loginCount, executedCommands, chatMessages;

    private Set<String> nameHistory, ipHistory;

    private Queue<ChatHistoryEntry> executedCommandHistory, chatHistory;

    private PlayerConnection lastPlayerConnection;

}