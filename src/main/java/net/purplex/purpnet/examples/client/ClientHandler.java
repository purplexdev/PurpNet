package net.purplex.purpnet.examples.client;

import io.netty.channel.Channel;
import net.purplex.purpnet.api.handler.NetHandler;

public class ClientHandler extends NetHandler {

    @Override
    public void onChannelEnable(Channel channel) {

    }

    @Override
    public void onChannelDisable(Channel channel) {

    }

    @Override
    public void onPacketRead(Channel channel, Object packet) {

    }

    @Override
    public void onPacketReadSuccess(Channel channel) {

    }

    @Override
    public void onExceptionCaught(Channel channel, Throwable cause) {

    }
}
