package net.purplex.purpnet.api;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import net.purplex.purpnet.Main;

public abstract class NetClient {

    protected NetClient() {
        Main.register(this);
    }

    public void connect(String host, int port) throws InterruptedException {
        ChannelFuture f = Main.nettyClient.connect(host, port);
    }

    public void disconnect() {
        Main.nettyClient.disconnect();
    }

    public void sendPacket(ByteBuf buf) {
        if (Main.nettyClient.serverChannel == null) {
            throw new IllegalStateException("Failed to send the packet, because you aren't connected to a server!");
        }
        Main.nettyClient.serverChannel.writeAndFlush(buf);
    }

    public abstract void onChannelEnable();

    public abstract void onChannelDisable();

    public abstract void onReceivePacket(ReceivePacketEvent event);

    public abstract void onException(Throwable cause);
}
