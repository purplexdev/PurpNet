package net.purplex.purpnet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import net.purplex.purpnet.api.NetClient;
import net.purplex.purpnet.api.ReceivePacketEvent;
import net.purplex.purpnet.client.NettyClient;

/**
 * @author purplex
 * @version 1.0.0
 * <p>
 * Date created: 19th May 2020
 * <p>
 * This project uses the latest version of netty
 */

public class Main {
    public static NettyClient nettyClient;

    public static NetClient netClient;

    public static void onChannelEnable(ChannelHandlerContext ctx) {
        if (netClient == null) {
            return;
        }
        netClient.onChannelEnable();
    }

    public static void onChannelDisable(ChannelHandlerContext ctx) {
        if (netClient == null) {
            return;
        }
        netClient.onChannelDisable();
    }

    public static void onReceivePacket(ChannelHandlerContext ctx, Object packet) {
        if(netClient == null) {
            return;
        }
        netClient.onReceivePacket(new ReceivePacketEvent(ctx, packet));
    }

    public static void onException(ChannelHandlerContext ctx, Throwable cause) {
        if(netClient == null) {
            return;
        }
        netClient.onException(cause);
    }

    public static void register(NetClient client) {
        netClient = client;
    }

}
