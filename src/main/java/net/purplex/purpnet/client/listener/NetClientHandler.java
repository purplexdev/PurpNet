package net.purplex.purpnet.client.listener;

import io.netty.channel.Channel;

public abstract class NetClientHandler {
    public abstract void onChannelEnable(Channel serverChannel);

    public abstract void onChannelDisable(Channel serverChannel);

    public abstract void onPacketRead(Channel serverChannel, Object packet);

    public abstract void onPacketReadSuccess(Channel serverChannel);

    public abstract void onExceptionCaught(Channel serverChannel,Throwable cause);
}
