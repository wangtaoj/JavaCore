package com.wangtao.datastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wangtao
 * Created on 2018/3/11
 **/
public class StringInputStream extends InputStream {

    private InputStream inputStream;

    public StringInputStream(String goal) {
        if(goal == null)
            throw new NullPointerException();
        this.inputStream = new ByteArrayInputStream(goal.getBytes());
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    public static void main(String[] args) throws Exception{
        InputStream is = new StringInputStream("abcdefg");
        int ch;
        while((ch = is.read()) != -1) {
            System.out.print((char)ch);
        }
    }
}
