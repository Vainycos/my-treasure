package com.vainycos;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Objects;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public Result() {

    }


    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> OK(T data) {
        Result<T> result = new Result<>(200, "ok");
        result.setData(data);
        return result;
    }

    public static <T> Result OK() {
        Result<T> result = new Result<>(200, "ok");
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static Result error(int code,String msg) {
        return new Result(code,msg);
    }

    public static Result error(String msg) {
        return new Result(500,msg);
    }


    public Result setData(T data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public T getData() {
        return this.data;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
