package com.wangtao.lambda.serialize;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author wangtao
 * Created at 2022/8/9 22:29
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
