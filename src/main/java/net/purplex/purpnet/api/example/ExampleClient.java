package net.purplex.purpnet.api.example;

import net.purplex.purpnet.api.NetClient;
import net.purplex.purpnet.api.ReceivePacketEvent;

public class ExampleClient extends NetClient {

    @Override
    public void onChannelEnable() {

    }

    @Override
    public void onChannelDisable() {

    }

    @Override
    public void onReceivePacket(ReceivePacketEvent event) {
        //ByteBuf buf = event.createEmptyDirectByteBuf();
       // event.sendPacket(buf);

        /**
         * Send the packet we received, back to the server
         */
        event.echo();
    }

    @Override
    public void onException(Throwable cause) {
        disconnect();
        cause.printStackTrace();
    }
}
