package net.purplex.purpnet.api.handler;

import io.netty.channel.Channel;

public abstract class NetHandler {
    /**
     * Called when the connection establishes
     * @param channel
     */
    public abstract void onChannelEnable(Channel channel);

    /**
     * Called when the connection closes
     * @param channel
     */
    public abstract void onChannelDisable(Channel channel);

    /**
     * Called when we read an incoming packet
     * @param channel
     * @param packet
     */
    public abstract void onPacketRead(Channel channel, Object packet);

    /**
     * Called once we successfully read an incoming packet.
     * After onPacketRead method
     * @param channel
     */
    public abstract void onPacketReadSuccess(Channel channel);


    /**
     * Called once an exception is caught
     * @param channel
     * @param cause
     */
    public abstract void onExceptionCaught(Channel channel,Throwable cause);
}
