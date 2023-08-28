package com.wangtao.nio;

/**
 * @author wangtao
 * Created at 2023/8/28 19:19
 */
public class DataNotCompletedException extends RuntimeException {

    public DataNotCompletedException() {
        super();
    }

    public DataNotCompletedException(String message) {
        super(message);
    }

    public DataNotCompletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
