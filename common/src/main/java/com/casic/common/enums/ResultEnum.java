package com.casic.common.enums;

public enum ResultEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESS(200,"成功"),
    WRONG_ROUTE(404,"路径错误"),
    NO_AUTH(403,"没有权限"),
    SYSTEM_ERROR(500,"系统错误"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
