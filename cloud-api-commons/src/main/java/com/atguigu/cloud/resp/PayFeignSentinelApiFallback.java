package com.atguigu.cloud.resp;

import com.atguigu.cloud.apis.PayFeignSentinelApi;
import org.springframework.stereotype.Component;

@Component
public class PayFeignSentinelApiFallback implements PayFeignSentinelApi {

    @Override
    public ResultData getPayByOrderNo(String orderNo) {
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), "服务器宕机了，fallback服务降级");
    }
}
