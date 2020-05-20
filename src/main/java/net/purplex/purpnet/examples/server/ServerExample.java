package net.purplex.purpnet.examples.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.purplex.purpnet.api.server.NetServer;

public class ServerExample {
    public static NetServer netServer;
    public static void main(String[] args) {
        netServer = new NetServer(new ServerHandler());

        while(netServer.connectedClients.size() == 0) {
            break;
        }

        //atleast one client has connected
        while(true) {
            ByteBuf buf = Unpooled.directBuffer();
            byte[] bytes = "hello".getBytes();
            buf.writeBytes(bytes);
            netServer.sendPacket(1, buf);
            netServer.sendPacketToAll(buf);


        }
    }

    @Deprecated
    private static void eat() {

    }
}
