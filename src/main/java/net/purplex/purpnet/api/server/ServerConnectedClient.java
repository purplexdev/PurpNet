package net.purplex.purpnet.api.server;

import io.netty.channel.Channel;
import net.purplex.purpnet.utils.IDUtils;

public class ServerConnectedClient {
    private final int id;
    private final Channel channel;

    /**
     * Create a server connected client, with given custom ID
     * @param id
     * @param channel
     */
    public ServerConnectedClient(final int id, final Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    /**
     * Create a server connected client, with an automatic generated ID,
     * Warning: Creating clients with automatic generated IDs are NOT
     * compatible with the server.
     * @param channel
     */
    public ServerConnectedClient(Channel channel) {
        this.id = IDUtils.generateRandomId();
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void disconnect() {
        channel.disconnect();
    }
}
