package com.chenxin.maker.mata;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/19 15:47
 * @modify
 */
public class MetaException extends RuntimeException{

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
