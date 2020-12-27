package com.lh.rpccore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Request implements Serializable {
    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;
}
