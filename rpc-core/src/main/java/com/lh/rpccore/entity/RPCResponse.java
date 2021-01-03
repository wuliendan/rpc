package com.lh.rpccore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResponse implements Serializable {
    private String message;

    private Object obj;
}
