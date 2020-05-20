package net.purplex.purpnet.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import net.purplex.purpnet.client.listener.NetClientHandler;

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


    public void connect(String host, int port) {
        final EventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap b = new Bootstrap();
        b.group(group).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new NetClientListener(NetClient.getInstance()));
            }
        });

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
            if(isHandlerRegistered()) {
                client.handler.onChannelEnable(ctx.channel());
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if(isHandlerRegistered()) {
                client.handler.onChannelDisable(ctx.channel());
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(isHandlerRegistered()) {
                client.handler.onPacketRead(ctx.channel(), msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            if(isHandlerRegistered()) {
                client.handler.onPacketReadSuccess(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if(isHandlerRegistered()) {
                client.handler.onExceptionCaught(ctx.channel(), cause);
            }
        }

        public boolean isHandlerRegistered() {
            return client != null && client.handler != null;
        }
    }


}
