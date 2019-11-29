package com.casic.client.common.enums;

public enum LogEnum {
    LOGIN(1,"登入操作"),
    LOGOUT(2,"登出操作"),
    SAVE(3,"保存操作"),
    UPDATE(4,"修改操作"),
    DELETE(5,"删除操作"),
    QUERY(6,"查询操作"),
    ;
    private Integer code;
    private String msg;

    LogEnum(Integer code, String msg) {
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
