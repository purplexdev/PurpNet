package net.purplex.purpnet.api.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.purplex.purpnet.api.handler.NetHandler;
import net.purplex.purpnet.utils.IDUtils;

import java.util.HashSet;

public class NetServer {
    public HashSet<ServerConnectedClient> connectedClients = new HashSet<ServerConnectedClient>();

    private static NetServer instance;

    private NetHandler handler;

    /**
     * Default constructor
     */
    public NetServer() {
        instance = this;
    }

    public NetServer(NetHandler handler) {
        instance = this;
        this.handler = handler;
    }


    public void sendPacket(ServerConnectedClient client, Object packet) {
        client.getChannel().writeAndFlush(packet);
    }

    public void writePacket(ServerConnectedClient client, Object packet) {
        client.getChannel().write(packet);
    }

    public void flushPacket(ServerConnectedClient client) {
        client.getChannel().flush();
    }

    public void sendPacket(int id, Object packet) {
        for(ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                sendPacket(client, packet);
                return;
            }
        }
    }

    public void writePacket(int id, Object packet) {
        for(ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                writePacket(client, packet);
                return;
            }
        }
    }

    public void flushPacket(int id) {
        for(ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                flushPacket(client);
                return;
            }
        }
    }


    public static NetServer getInstance() {
        return instance;
    }

    class NetServerListener extends ChannelInboundHandlerAdapter {
        private NetServer server;
        NetServerListener(NetServer server) {
            this.server = server;
        }
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                server.handler.onChannelEnable(ctx.channel());
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                server.handler.onChannelDisable(ctx.channel());
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (isHandlerRegistered()) {
                server.handler.onPacketRead(ctx.channel(), msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                server.handler.onPacketReadSuccess(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (isHandlerRegistered()) {
                server.handler.onExceptionCaught(ctx.channel(), cause);
            }
        }

        public boolean isHandlerRegistered() {
            return server != null && server.handler != null;
        }
    }




}
