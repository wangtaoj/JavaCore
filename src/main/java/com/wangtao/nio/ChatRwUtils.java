package com.wangtao.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * @author wangtao
 * Created at 2023/8/28 19:24
 */
public final class ChatRwUtils {

    private ChatRwUtils() {

    }

    public static ByteBuffer createByteBuffer(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return ByteBuffer.allocate(0);
        }
        ByteBuffer buf = ByteBuffer.allocate(4 + bytes.length);
        buf.putInt(bytes.length);
        buf.put(bytes);

        buf.flip();
        return buf;
    }

    public static ByteBuffer readChannel(ReadableByteChannel channel) throws IOException {
        ByteBuffer sizeBuf = ByteBuffer.allocate(4);
        int len = ChannelUtils.readFully(channel, sizeBuf);
        if (len == 0) {
            return ByteBuffer.allocate(0);
        }
        // 读取4个字节, 内容大小
        int size = sizeBuf.getInt(0);
        // 读取内容
        ByteBuffer dataBuf = ByteBuffer.allocate(size);
        len = ChannelUtils.readFully(channel, dataBuf);
        if (len == 0) {
            throw new DataNotCompletedException();
        }
        dataBuf.flip();
        return dataBuf;
    }
}
