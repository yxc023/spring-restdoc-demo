package com.yangxiaochen.demo.spring.resetdocs;

import lombok.Data;

@Data
public class Result<T> {

    private int code = 0;
    private String message;
    private T data;

    public Result<T> withData(T data) {
        this.data = data;
        return this;
    }

    public static Result success() {
        return new Result();
    }

    public static Result failed(String message){
        Result result = new Result();
        result.setCode(-1);
        result.setMessage(message);

        return result;
    }
}
