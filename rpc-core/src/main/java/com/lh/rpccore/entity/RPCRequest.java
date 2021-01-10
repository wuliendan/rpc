package com.lh.rpccore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCRequest implements Serializable {

    /**
     * 请求id.
     */
    private String requestId;

    /**
     * 请求服务类型.
     */
    private String className;

    /**
     * 请求方法名称.
     */
    private String methodName;

    /**
     * 请求方法参数类型数组.
     */
    private Class<?>[] paramTypes;

    /**
     * 请求参数列表.
     */
    private Object[] params;
}
