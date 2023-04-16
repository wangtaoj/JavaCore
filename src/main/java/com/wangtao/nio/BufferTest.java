package com.wangtao.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author wangtao
 * Created at 2023/4/11 20:20
 */
public class BufferTest {

    /**
     * ByteBuffer分为HeapByteBuffer、DirectByteBuffer
     * HeapByteBuffer操作的是JVM内存
     * DirectByteBuffer直接操作操作系统内存
     * DirectByteBuffer不能直接拿到内部byte数组，即调用array方法会报错，内部的byte数组为null
     * 可以通过hasArray做一个前置判断
     * ByteBuffer内部有3个重要属性
     * position: 下一个要写或者读的位置
     * limit: 读或者写的最大值
     * capacity: 容量
     */
    @Test
    public void readWrite() {
        // 创建一个HeapByteBuffer(position = 0, limit = capacity = 128)
        ByteBuffer buffer = ByteBuffer.allocate(128);
        // 创建一个DirectByteBuffer
        // ByteBuffer buffer = ByteBuffer.allocateDirect(128);
        printBuffer(buffer);
        System.out.println("write mode: ");
        // 往buffer里添加数据, position会往后移动
        buffer.put("hello".getBytes());
        printBuffer(buffer);
        System.out.println("read mode: ");
        // 切成读模式, 即limit = position, position = 0
        buffer.flip();
        printBuffer(buffer);
        // HeapByteBuffer可以直接获取到内部的byte数组
        System.out.println(new String(buffer.array(), buffer.position(), buffer.limit()));
        printBuffer(buffer);
        // 使用get方法读取, position会往后移动
        byte[] content = new byte[buffer.limit()];
        buffer.get(content);
        printBuffer(buffer);
        System.out.println(new String(content));
    }

    /**
     * mark <= position <= limit <= capacity
     * 标记和重置
     * 使用mark记住position, reset退回去重新做一些事情
     * 比如输入abchello
     * 希望输出abchello  hello
     */
    @Test
    public void markReset() {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        printBuffer(buffer);
        buffer.put("abchello".getBytes());
        // 切成读模式
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        int i = 0;
        while (buffer.hasRemaining()) {
            bytes[i++] = buffer.get();
            if (i == 3) {
                // 标记
                buffer.mark();
            }
            if (!buffer.hasRemaining()) {
                String str = new String(bytes, 0, i);
                System.out.println(str);
                // 退回到3的位置, 重新读取
                buffer.reset();
                i = 0;
                if ("hello".equals(str)) {
                    break;
                }
            }
        }
    }

    /**
     * rewind: 退回, position直接退回到0
     * mark搭配reset更为灵活, rewind只能退回到0
     */
    @Test
    public void rewind() {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        printBuffer(buffer);
        buffer.put("hello".getBytes());
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(new String(bytes));
        // 退回去, 再读一遍
        buffer.rewind();
        System.out.println(new String(bytes));
    }

    /**
     * 清空buffer
     * position = 0, limit = capacity, mark = -1
     * 注意: 不会清空内部数组里的内容
     */
    @Test
    public void clear() {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        printBuffer(buffer);
        buffer.put("hello".getBytes());
        buffer.clear();
    }

    private void printBuffer(ByteBuffer buffer) {
        System.out.printf("position: %s, limit: %s, capacity: %s\n", buffer.position(), buffer.limit(), buffer.capacity());
    }
}
