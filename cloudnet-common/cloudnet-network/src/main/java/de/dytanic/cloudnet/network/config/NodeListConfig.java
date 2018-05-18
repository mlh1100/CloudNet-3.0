package de.dytanic.cloudnet.network.config;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.network.components.NetworkNode;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Predicate;

public final class NodeListConfig {

    private static final Type TYPE = new TypeToken<Collection<NetworkNode>>() {
    }.getType();

    private Path configFilePath;

    private Document document;

    public NodeListConfig(Path path)
    {
        this.configFilePath = path;
        this.document = null;
    }

    public void addNode(NetworkNode networkNode)
    {
        initCachedDocument();

        Collection<NetworkNode> networkNodes = this.document.getObject("nodes", TYPE);

        NetworkNode node = $.filterFirst(networkNodes, new Predicate<NetworkNode>() {
            @Override
            public boolean test(NetworkNode netNode)
            {
                return netNode.getUniqueId().equals(networkNode.getUniqueId());
            }
        });

        if (node != null) networkNodes.remove(node);

        networkNodes.add(networkNode);
        this.document.append("nodes", networkNodes).save(configFilePath);
    }

    public void setNodeList(Collection<NetworkNode> nodeList)
    {
        initCachedDocument();

        this.document.append("nodes", nodeList).save(configFilePath);
    }

    public Collection<NetworkNode> getNodeList()
    {
        initCachedDocument();

        return this.document.getObject("nodes", TYPE);
    }

    private void initCachedDocument()
    {
        if (document == null)
            if (!Files.exists(configFilePath))
            {
                this.document = new Document("nodes", new JsonArray());
                this.document.save(configFilePath);

            } else this.document = Document.newDocument(configFilePath);
    }
}