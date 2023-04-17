package com.wangtao.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 内存映射
 * @author wangtao
 * Created at 2023/4/11 21:08
 */
public class MmapTest {

    /**
     * 写文件
     */
    @Test
    public void mmapWrite() {
        // 以读写模式打开一个通道, 文件不存在则新建, 如果存在先清空内容
        try (FileChannel channel = FileChannel.open(Paths.get("hello.txt"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            System.out.println("size: " + channel.size());
            /*
             * 得到的buffer position=0, limit=capacity=size
             * position: 起始位置
             * size: 映射区域的大小
             */
            byte[] bytes = "Hello World!\nHello Buffer\n".getBytes();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
            printBuffer(buffer);
            buffer.put(bytes);
            // 刷新到磁盘
            buffer.force();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void mmapRead() {
        try (FileChannel channel = FileChannel.open(Paths.get("hello.txt"), StandardOpenOption.READ)) {
            System.out.println("size: " + channel.size());
            /*
             * 得到的buffer position=0, limit=capacity=size
             * position: 起始位置
             * size: 映射区域的大小
             */
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            printBuffer(buffer);
            byte[] dst = new byte[buffer.remaining()];
            buffer.get(dst);
            System.out.println(new String(dst));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 追加文件
     */
    @Test
    public void mmapAppend() {
        // 以读写模式打开一个通道
        try (FileChannel channel = FileChannel.open(Paths.get("hello.txt"),
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            long oldSize = channel.size();
            System.out.println("size: " + oldSize);
            /*
             * 得到的buffer position=0, limit=capacity=size
             * position: 起始位置
             * size: 映射区域的大小
             */
            byte[] bytes = "Hello Mmap!\n".getBytes();
            long newSize = oldSize + bytes.length;
            // channel.size()也会被修改
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, newSize);
            printBuffer(buffer);
            // 移动buffer position, 以便追加内容, 而不是覆盖
            buffer.position((int)oldSize);
            buffer.put(bytes);
            buffer.force();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 追加文件
     * 映射的内存区域为通道的末尾
     */
    @Test
    public void mmapAppend1() {
        // 以读写模式打开一个通道
        try (FileChannel channel = FileChannel.open(Paths.get("hello.txt"),
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            long oldSize = channel.size();
            System.out.println("size: " + oldSize);
            /*
             * 得到的buffer position=0, limit=capacity=size
             * position: 起始位置
             * size: 映射区域的大小
             */
            byte[] bytes = "Hello Mmap!\n".getBytes();
            long newSize = bytes.length;
            // channel.size()也会被修改
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, oldSize, newSize);
            System.out.println("size: " + channel.size());
            printBuffer(buffer);
            buffer.put(bytes);
            buffer.force();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printBuffer(ByteBuffer buffer) {
        System.out.printf("position: %s, limit: %s, capacity: %s\n", buffer.position(), buffer.limit(), buffer.capacity());
    }
}
