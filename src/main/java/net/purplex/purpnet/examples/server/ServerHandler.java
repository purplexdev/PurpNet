package net.purplex.purpnet.examples.server;

import io.netty.channel.Channel;
import net.purplex.purpnet.api.handler.NetHandler;

public class ServerHandler extends NetHandler {
    @Override
    public void onChannelEnable(Channel channel) {

    }

    @Override
    public void onChannelDisable(Channel channel) {

    }

    @Override
    public void onPacketRead(Channel channel, Object packet) {
       // ByteBuf p = (ByteBuf)packet;
        System.out.println("Received packet: " + packet.toString());
    }

    @Override
    public void onPacketReadSuccess(Channel channel) {

    }

    @Override
    public void onExceptionCaught(Channel channel, Throwable cause) {
        channel.close();
        cause.printStackTrace();
    }
}
