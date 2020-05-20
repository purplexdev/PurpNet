package net.purplex.purpnet.api.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.purplex.purpnet.api.handler.NetHandler;

import java.util.HashSet;

public class NetServer {
    /**
     * Set of all current connected clients
     */
    public HashSet<ServerConnectedClient> connectedClients = new HashSet<ServerConnectedClient>();

    private NetHandler handler;

    /**
     * Default constructor
     */
    public NetServer() {

    }

    public NetServer(final NetHandler handler) {
        this.handler = handler;
    }


    public void sendPacket(final ServerConnectedClient client, final Object packet) {
        client.getChannel().writeAndFlush(packet);
    }

    public void writePacket(final ServerConnectedClient client, final Object packet) {
        client.getChannel().write(packet);
    }

    public void flushPacket(final ServerConnectedClient client) {
        client.getChannel().flush();
    }

    public void sendPacket(final int id, Object packet) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                sendPacket(client, packet);
                return;
            }
        }
    }

    public void writePacket(final int id, Object packet) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                writePacket(client, packet);
                return;
            }
        }
    }

    public void flushPacket(final int id) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                flushPacket(client);
                return;
            }
        }
    }

    private void addClient(Channel channel) {
        ServerConnectedClient client = new ServerConnectedClient(channel);
        connectedClients.add(client);
    }

    private void removeClient(Channel channel) {
        for(ServerConnectedClient client : connectedClients) {
            if(client.getChannel().id() == channel.id()) {
                removeClient(client);
                return;
            }
        }
    }

    private void removeClient(ServerConnectedClient client) {
        connectedClients.remove(client);
    }


    class NetServerListener extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                handler.onChannelEnable(ctx.channel());
            }
            addClient(ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                handler.onChannelDisable(ctx.channel());
            }
            removeClient(ctx.channel());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (isHandlerRegistered()) {
                handler.onPacketRead(ctx.channel(), msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                handler.onPacketReadSuccess(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (isHandlerRegistered()) {
                handler.onExceptionCaught(ctx.channel(), cause);
            }
        }

        public boolean isHandlerRegistered() {
            return handler != null;
        }
    }




}
