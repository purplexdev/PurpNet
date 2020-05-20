package net.purplex.purpnet.api.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.purplex.purpnet.api.annotations.NetWarning;
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

    /**
     * Constructor with a registered NetHandler
     * @param handler
     */
    public NetServer(final NetHandler handler) {
        this.handler = handler;
    }


    /**
     * Writes and flushes a packet to the client
     *
     * @param packet
     */
    public void sendPacket(final ServerConnectedClient client, final Object packet) {
        client.getChannel().writeAndFlush(packet);
    }

    /**
     * Only writes the packet to the client
     *
     * @param packet
     */
    public void writePacket(final ServerConnectedClient client, final Object packet) {
        client.getChannel().write(packet);
    }

    /**
     * Only flushes
     */
    public void flushPacket(final ServerConnectedClient client) {
        client.getChannel().flush();
    }

    /**
     * Writes and flushes a packet to the client
     *
     * @param packet
     */
    @NetWarning
    public void sendPacket(final int id, Object packet) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                sendPacket(client, packet);
                return;
            }
        }
    }

    /**
     * Only writes the packet
     * @param id
     * @param packet
     */
    @NetWarning
    public void writePacket(final int id, Object packet) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                writePacket(client, packet);
                return;
            }
        }
    }
    /**
     * Only flushes
     */
    @NetWarning
    public void flushPacket(final int id) {
        for(final ServerConnectedClient client : connectedClients) {
            if(client.getId() == id) {
                flushPacket(client);
                return;
            }
        }
    }

    /**
     * Writes and flushes the packet to all connected clients
     * @param packet
     */
    public void sendPacketToAll(Object packet) {
        for(final ServerConnectedClient client : connectedClients) {
            sendPacket(client, packet);
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

        private boolean isHandlerRegistered() {
            return handler != null;
        }
    }




}
