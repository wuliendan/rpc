package com.lh.rpccore.entity;

public enum RPCResponseCodeEnum {

    SUCCESS(0, "success"),

    FAIL(1, "fail");

    /**
     * code码.
     */
    private int code;

    /**
     * 服务调用结果消息.
     */
    private String message;

    RPCResponseCodeEnum(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * get code.
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * set code.
     *
     * @param code code
     */
    public void setCode(final int code) {
        this.code = code;
    }

    /**
     * get message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message.
     *
     * @param message message
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
