package net.purplex.purpnet.api.handler;

import io.netty.channel.Channel;

public abstract class NetHandler {
    public abstract void onChannelEnable(Channel serverChannel);

    public abstract void onChannelDisable(Channel serverChannel);

    public abstract void onPacketRead(Channel serverChannel, Object packet);

    public abstract void onPacketReadSuccess(Channel serverChannel);

    public abstract void onExceptionCaught(Channel serverChannel,Throwable cause);
}
