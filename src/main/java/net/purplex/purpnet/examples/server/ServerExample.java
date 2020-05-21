package net.purplex.purpnet.examples.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.purplex.purpnet.api.server.NetServer;

public class ServerExample {

    private static long lastTime;

    public static NetServer netServer;

    public static void main(String[] args) {
        //register the serverhandler, used for listening to packets
        netServer = new NetServer(new ServerHandler());

        while (netServer.connectedClients.size() == 0) {
            System.out.println("no one has connected");
        }

        //atleast one client has connected
        while (hasOneSecondPassed()) {
            ByteBuf buf = Unpooled.directBuffer();
            byte[] bytes = "hello".getBytes();
            buf.writeBytes(bytes);
            //send the packet to all
            netServer.sendPacketToAll(buf); //send to all
        }
    }

    private static boolean hasOneSecondPassed() {
        final long curTime = System.currentTimeMillis();
        if(curTime - lastTime >= 1000L) {
            lastTime = curTime;
            return true;
        }
        return false;
    }
}
