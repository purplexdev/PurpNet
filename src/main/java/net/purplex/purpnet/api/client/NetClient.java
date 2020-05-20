package net.purplex.purpnet.api.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.purplex.purpnet.api.exception.ChannelDisconnectException;
import net.purplex.purpnet.api.handler.NetHandler;

public class NetClient {

    private static NetClient instance;

    private NetHandler handler;

    private Channel serverChannel;

    private boolean isConnected;

    /**
     * Default constructor
     */
    public NetClient() {
        instance = this;
    }

    /**
     * Constructor with a registered packet listener
     *
     * @param handler
     */
    public NetClient(final NetHandler handler) {
        instance = this;
        this.handler = handler;
    }

    /**
     * Connect to the host and port given in as arguments
     * Returns a ChannelFuture
     * @param host
     * @param port
     * @throws InterruptedException
     */
    public ChannelFuture connect(final String host, final int port) throws InterruptedException {
        final EventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap b = new Bootstrap();
        ChannelFuture f = b.
                group(group).
                channel(NioSocketChannel.class).
                handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NetClientListener(NetClient.getInstance()));
                    }
                }).connect(host, port).sync();
        this.serverChannel = f.channel();
        isConnected = true;
        return f;
    }

    /**
     * Disconnect from the current connected server
     * Throws an exception if you are not connected to a server
     */
    public void disconnect() {
        final ChannelDisconnectException.ChannelDisconnectExceptionReason reason;
        if (!isConnected) {
            reason = ChannelDisconnectException.ChannelDisconnectExceptionReason.NOT_CONNECTED;
        } else if (serverChannel == null) {
            reason = ChannelDisconnectException.ChannelDisconnectExceptionReason.NULL_SERVER_CHANNEL;
        } else {
            serverChannel.disconnect();
            return;
        }
        throw new ChannelDisconnectException(reason);
    }

    /**
     * Writes and flushes a packet
     *
     * @param packet
     */
    public void sendPacket(final Object packet) {
        serverChannel.writeAndFlush(packet);
    }

    /**
     * Only writes the packet
     *
     * @param packet
     */
    public void writePacket(final Object packet) {
        serverChannel.write(packet);
    }

    /**
     * Only flushes
     */
    public void flushPacket() {
        serverChannel.flush();
    }

    /**
     * Returning the client's instance
     * @return
     */
    public static NetClient getInstance() {
        return instance;
    }


    class NetClientListener extends ChannelInboundHandlerAdapter {
        private final NetClient client;

        NetClientListener(final NetClient client) {
            this.client = client;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                client.handler.onChannelEnable(ctx.channel());
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                client.handler.onChannelDisable(ctx.channel());
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (isHandlerRegistered()) {
                client.handler.onPacketRead(ctx.channel(), msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            if (isHandlerRegistered()) {
                client.handler.onPacketReadSuccess(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (isHandlerRegistered()) {
                client.handler.onExceptionCaught(ctx.channel(), cause);
            }
        }

        public boolean isHandlerRegistered() {
            return client != null && client.handler != null;
        }
    }


}
