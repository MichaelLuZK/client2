package com.casic.common.exception;


import com.casic.common.enums.ResultEnum;

public class MyException extends RuntimeException{//继承runtimeException是因为spring框架只对runtime异常进行事物回滚
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }
}
