package com.wangtao.net;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author wangtao
 * Created at 2019/3/17 11:24
 */
public class INetAddressTest {

    @Test
    public void api() {
        try {
            // 根据主机名创建地址, 需要与本地DNS打交道
            InetAddress address = InetAddress.getByName("www.baidu.com");
            // 主机名 + IP地址
            System.out.println(address.getHostName() + "/" + address.getHostAddress());
            // IP地址, 4字节, 因为存的是byte, 超过127的数字会被强转为byte而变成负数, 结果会与getHostAddress()有点差别
            byte[] ipBytes = address.getAddress();
            System.out.println(Arrays.toString(address.getAddress()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ipBytes.length; i++) {
                if(i == 0)
                    sb.append(ipBytes[i] & 0xff);
                else
                    sb.append('.').append(ipBytes[i] & 0xff);
            }
            System.out.println(sb.toString());
            System.out.println("-------------------------");

            // 根据主机地址创建, 不需要与本地DNS打交道
            address = InetAddress.getByName(address.getHostAddress());
            System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
            System.out.println("------------------------------");

            address = InetAddress.getByAddress(new byte[]{115, (byte)239, (byte)210, 27});
            System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
            System.out.println("------------------------------");

            // 获取本机地址
            address = InetAddress.getLocalHost();
            System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
            System.out.println(Arrays.toString(address.getAddress()));
            System.out.println("-------------------------------------");

            // 获取环回地址
            address = InetAddress.getLoopbackAddress();
            System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
            System.out.println(Arrays.toString(address.getAddress()));
            System.out.println("-------------------------------------");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
