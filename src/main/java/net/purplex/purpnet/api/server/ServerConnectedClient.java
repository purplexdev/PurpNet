package net.purplex.purpnet.api.server;

import io.netty.channel.Channel;

public class ServerConnectedClient {
    private int id;
    private Channel channel;

    public ServerConnectedClient(int id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }
}
