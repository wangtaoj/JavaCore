package com.wangtao.nio.nonblocking;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangtao
 * Created at 2023/8/28 21:19
 */
@Data
public class ExchangeContext {

    private AtomicInteger processCount = new AtomicInteger(0);

    private byte[] datas;
}
