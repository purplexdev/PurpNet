package net.purplex.purpnet.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ReceivePacketEvent {
    private ChannelHandlerContext ctx;
    private Object packet;

    public ReceivePacketEvent(ChannelHandlerContext ctx, Object packet) {
        this.ctx = ctx;
        this.packet = packet;
    }

    public Channel getChannel() {
        return ctx.channel();
    }

    public Object getPacket() {
        return packet;
    }

    public ByteBuf createEmptyDirectByteBuf() {
        return ctx.alloc().directBuffer();
    }

    public ByteBuf createUnpooledByteBuf() {
        return Unpooled.buffer();
    }

    public void echo() {
        sendPacket((ByteBuf)packet);
    }
    public void cancel() {
        ByteBuf buf = (ByteBuf)packet;
        buf.release();
    }

    /**
     * Sends a packet back to the server using the ChannelHandlerContext
     * @param buf ByteBuf
     */
    public void sendPacket(ByteBuf buf) {
        ctx.writeAndFlush(buf);
    }
}
