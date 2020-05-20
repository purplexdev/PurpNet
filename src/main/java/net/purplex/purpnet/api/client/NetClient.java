package net.purplex.purpnet.api.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.purplex.purpnet.api.client.listener.NetClientHandler;

/**
 * Instance of your client
 */
public class NetClient {

    private static NetClient instance;

    private NetClientHandler handler;

    private Channel serverChannel;

    private boolean isConnected;

    /**
     * Default constructor
     */
    public NetClient() {
        instance = this;
    }

    public NetClient(NetClientHandler handler) {
        instance = this;
        this.handler = handler;
    }


    public void connect(String host, int port) throws InterruptedException {
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
    }

    public void disconnect() {
        if(!isConnected) {
            System.err.println("You are not connected to a server, failed to disconnect!");
        }
        else if(serverChannel == null) {
            System.err.println("The server's channel is null, failed to disconnect!");
        }
        else  {
            serverChannel.disconnect();
        }
    }

    public void sendPacket(Object packet) {
        serverChannel.writeAndFlush(packet);
    }

    public static NetClient getInstance() {
        return instance;
    }


    class NetClientListener extends ChannelInboundHandlerAdapter {
        private NetClient client;

        public NetClientListener(NetClient client) {
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
