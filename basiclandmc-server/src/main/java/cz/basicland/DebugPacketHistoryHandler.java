package cz.basicland;

import com.mojang.logging.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringJoiner;

public class DebugPacketHistoryHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int capacity;
    private final Deque<String> recent;

    public DebugPacketHistoryHandler(int capacity) {
        this.capacity = Math.max(8, capacity);
        this.recent = new ArrayDeque<>(this.capacity);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        record("CHANNEL_ACTIVE");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        record("CHANNEL_INACTIVE");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // keep message info lightweight to avoid OOM/log spam
        String msgInfo = summarize(msg);
        record("READ -> " + msgInfo);
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Log history on ReadTimeoutException or any error that causes disconnect
        if (cause instanceof ReadTimeoutException) {
            logFullDebug(ctx, "ReadTimeoutException", cause);
        }
        super.exceptionCaught(ctx, cause);
    }

    private void record(String entry) {
        String line = Instant.now().toString() + " " + entry;
        synchronized (recent) {
            if (recent.size() >= capacity) {
                recent.removeFirst();
            }
            recent.addLast(line);
        }
    }

    private String summarize(Object msg) {
        if (msg == null) return "null";
        String cls = msg.getClass().getSimpleName();
        try {
            // safe length info for common Netty types
            if (msg instanceof io.netty.buffer.ByteBuf buf) {
                return cls + "(bytes=" + buf.readableBytes() + ")";
            }
            if (msg instanceof java.util.List<?> list) {
                return cls + "(size=" + list.size() + ")";
            }
        } catch (Throwable ignored) {}
        // avoid calling heavy toString()
        return cls;
    }

    private void logFullDebug(ChannelHandlerContext ctx, String reason, Throwable cause) {
        SocketAddress remote = ctx.channel().remoteAddress();
        StringJoiner sj = new StringJoiner("\n");
        sj.add("==== DebugPacketHistoryHandler dump ====");
        sj.add("Reason: " + reason);
        sj.add("Remote: " + remote);
        sj.add("Recent inbound events (most recent last):");
        synchronized (recent) {
            recent.forEach(sj::add);
        }
        sj.add("Thread snapshot (current thread):");
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            sj.add("  at " + e.toString());
        }
        LOGGER.warn(sj.toString(), cause);
    }
}
