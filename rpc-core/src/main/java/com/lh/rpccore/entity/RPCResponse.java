package com.lh.rpccore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResponse implements Serializable {

    /**
     * 请求id.
     */
    private String requestId;

    /**
     * 响应状态码.
     */
    private int code;

    /**
     * 响应消息说明.
     */
    private String message;

    /**
     * 响应数据.
     */
    private Object obj;
}
