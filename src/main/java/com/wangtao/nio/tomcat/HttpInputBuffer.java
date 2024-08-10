package com.wangtao.nio.tomcat;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
public class HttpInputBuffer {

    /**
     * 最大的bufferSize, 8kb，每次请求数据不能超过这个大小
     */
    private final int MAX_BUFFER_SIZE = 1024 * 1024 * 8;

    private final ByteBuffer buf = ByteBuffer.allocate(MAX_BUFFER_SIZE + 1);

    private final SocketChannelWrap socketChannelWrap;

    public HttpInputBuffer(SocketChannelWrap socketChannelWrap) {
        this.socketChannelWrap = socketChannelWrap;
    }

    /**
     * 简化逻辑，处理一行数据，只要检测到以\n结尾，则代表一次请求结束
     * 注意: 没有兼容\r\n换行符
     */
    public String parseRequestData() throws IOException {
        int dataSize = 0;
        while (true) {
            int nRead = socketChannelWrap.getSocketChannel().read(buf);
            socketChannelWrap.updatelastReadTimestamp();
            if (nRead == -1) {
                throw new EOFException();
            }
            if (nRead == 0) {
                if (!buf.hasRemaining()) {
                    throw new IllegalArgumentException("请求数据大小超出最大值");
                }
                // 没有数据了， 等待再次调度
                socketChannelWrap.registerReadEvent();
                return null;
            }
            int lastPosition = buf.position();
            // 切换成读模式
            buf.flip();
            boolean isComplete = false;
            while (buf.hasRemaining()) {
                byte b = buf.get();
                if (b == '\n') {
                    isComplete = true;
                    break;
                }
                dataSize++;
            }
            if (isComplete) {
                // buf中还有数据
                if (buf.hasRemaining()) {
                    throw new IllegalArgumentException("请求数据必须使用\\n为结尾");
                }
                break;
            } else {
                // 请求数据不完整, 继续读取通道数据
                buf.limit(buf.capacity());
                buf.position(lastPosition);
            }
        }
        return new String(buf.array(), 0, dataSize);
    }

    public void reset() {
        buf.clear();
    }
}
