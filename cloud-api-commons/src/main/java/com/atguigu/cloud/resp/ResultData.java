package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T> {
    private String code; // 结果状态
    private String message;
    private T data;
    private long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResultData<T> success(T data) {
        ResultData resultData = new ResultData();

        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);

        return resultData;
    }

    public static <T> ResultData<T> fail(String code, String message) {
        ResultData resultData = new ResultData();

        resultData.setCode(code);
        resultData.setMessage(message);
        resultData.setData(null);

        return resultData;
    }

    public static <T> ResultData<T> fail(String message) {
        ResultData resultData = new ResultData();

        resultData.setCode("9999");
        resultData.setMessage(message);
        resultData.setData(null);

        return resultData;
    }

    public static <T> ResultData<T> fail() {
        ResultData resultData = new ResultData();

        resultData.setCode(ReturnCodeEnum.RC999.getCode());
        resultData.setMessage(ReturnCodeEnum.RC999.getMessage());
        resultData.setData(null);

        return resultData;
    }

}
