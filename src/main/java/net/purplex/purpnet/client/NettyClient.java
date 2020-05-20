package net.purplex.purpnet.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import net.purplex.purpnet.handlers.NettyClientHandler;

public class NettyClient {

    public Channel serverChannel;

    private EventLoopGroup group;

    public ChannelFuture connect(String host, int port) throws InterruptedException {
        final Bootstrap bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        ChannelFuture f = bootstrap.group(group).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyClientHandler());
            }
        }).connect(host, port).sync();
        serverChannel = f.channel();
        return f;
    }

    /**
     * Only throws the illegal state exception when you try disconnecting while not connected to any server
     * @throws IllegalStateException
     */
    public void disconnect() throws IllegalStateException {
        if (serverChannel == null || !serverChannel.isOpen()) {
            throw new IllegalStateException("You are not connected to any server!");
        }
        try {
            serverChannel.disconnect().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        group.shutdownGracefully();
    }
}
