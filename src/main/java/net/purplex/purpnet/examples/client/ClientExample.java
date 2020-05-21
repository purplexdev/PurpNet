package net.purplex.purpnet.examples.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.purplex.purpnet.api.client.NetClient;

public class ClientExample {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8080;

    private static NetClient netClient;

    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        //register the clienthandler used for listening to packets
        netClient = new NetClient(new ClientHandler());

        try {
            netClient.connect(HOST, PORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while(System.currentTimeMillis() - startTime < 100000L) { //run for 100 seconds
            ByteBuf buf = Unpooled.directBuffer();
            byte[] bytes = "hello".getBytes();
            buf.writeBytes(bytes);
            netClient.sendPacket(buf);
            ;
        }

        netClient.disconnect();

    }
}
