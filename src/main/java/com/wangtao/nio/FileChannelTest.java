package com.wangtao.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author wangtao
 * Created at 2019/6/4 21:51
 */
public class FileChannelTest {

    /**
     * 从指定文件中读取一个内容
     */
    @Test
    public void read() {
        try (FileChannel channel = FileChannel.open(Paths.get("README.md"), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 从channel中读取数据写入到buffer中
            while(channel.read(buffer) > 0) {
                // 切成读模式
                buffer.flip();
                System.out.print(new String(buffer.array(), buffer.position(), buffer.limit()));
                // 清空, 以便下次写入
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * CREATE: 不存在则先创建
     * CREATE_NEW: 创建一个新文件, 若存在则抛异常
     * WRITE: 写权限, 从文件开始写入, 不会清空文件内容
     * APPEND: 写权限, 从文件末尾写入
     */
    @Test
    public void write() {
        // 创建一个文件, 如果文件存在, 先清空文件, 然后在文件开头写入
        try (FileChannel channel = FileChannel.open(Paths.get("hello.txt"), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello world\nhello channel\n".getBytes());
            // 切成读模式
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制一个文件
     */
    @Test
    public void copy() {
        // 创建一个FileChannel, 如果指定的文件存在, 则以追加的方式写入
        // 如果不存在, 先创建, 然后追加
        try (FileChannel readChannel = FileChannel.open(Paths.get("README.md"), StandardOpenOption.READ);
             FileChannel writeChannel = FileChannel.open(Paths.get("D:\\copy.md"),
                     StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            // 将自己通道内的内容写入到给定的通道中, 此方法不会改变本通道的位置
            readChannel.transferTo(0, readChannel.size(), writeChannel);
            // 因为没有改变位置, 可以继续从该通道中读取数据
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while(readChannel.read(buf) > 0) {
                buf.flip();
                System.out.print(new String(buf.array(), buf.position(), buf.limit()));
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
