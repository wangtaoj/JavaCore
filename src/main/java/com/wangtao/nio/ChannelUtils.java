package com.wangtao.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023/8/27 21:47
 */
@Slf4j
public final class ChannelUtils {

    private ChannelUtils() {

    }

    /**
     * 关闭资源
     * @param resource 资源
     */
    public static void closeQuietly(Closeable resource) {
        if (Objects.nonNull(resource)) {
            try {
                resource.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 从通道读取数据
     * 如果知道要读取多少字节数据, 使用此方法将数据全部读取到缓冲区中
     * 只有填满缓冲区或者到达流末尾方法才会结束
     *
     * @param channel 通道
     * @param buf 缓冲区
     * @return 读取的字节数, 第一次读取时便已经到达流末尾则返回0
     * @throws IOException 异常信息
     * @throws DataNotCompletedException 如果读取到数据但是没有填满缓冲区便已经到达流末尾则抛异常
     */
    public static int readFully(ReadableByteChannel channel, ByteBuffer buf) throws IOException {
        int total = 0;
        while (buf.hasRemaining()) {
            int len = channel.read(buf);
            if (len > 0) {
                total += len;
            } else if (len == -1) {
                break;
            }
        }
        if (total > 0 && buf.hasRemaining()) {
            throw new DataNotCompletedException();
        }
        return total;
    }

    /**
     * 完整将数据写入到通道
     * 非阻塞模式下普通的write方法不保证会将数据全部写入到通道中
     * @param channel 通道
     * @param buf 缓冲区
     * @throws IOException 异常信息
     */
    public static void writeFully(WritableByteChannel channel, ByteBuffer buf) throws IOException {
        while (buf.hasRemaining()) {
            // 写入方法可能返回0，如果通道中可写入的缓冲区满了，阻塞模式将阻塞，非阻塞模式将返回0
            channel.write(buf);
        }
    }

    /**
     * 完整将数据写入到通道
     * 非阻塞模式下普通的write方法不保证会将数据全部写入到通道中
     * @param channel 通道
     * @param bufs 缓冲区数组
     * @throws IOException 异常信息
     */
    public static void writeFully(GatheringByteChannel channel, ByteBuffer ...bufs) throws IOException {
        if (bufs == null || bufs.length == 0) {
            return;
        }
        ByteBuffer lastBuf = bufs[bufs.length - 1];
        while (lastBuf.hasRemaining()) {
            // 写入方法可能返回0，如果通道中可写入的缓冲区满了，阻塞模式将阻塞，非阻塞模式将返回0
            channel.write(bufs);
        }
    }
}
