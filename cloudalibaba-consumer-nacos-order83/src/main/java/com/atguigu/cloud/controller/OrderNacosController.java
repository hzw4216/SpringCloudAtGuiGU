package com.atguigu.cloud.controller;

import com.atguigu.cloud.apis.PayFeignSentinelApi;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderNacosController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @GetMapping("/order/pay/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(serverURL + "/pay/nacos/" + id, String.class)+"\t"+"我是OrderNacosController83调用者。。。。";
    }

    @Resource
    private PayFeignSentinelApi payFeignSentinelApi;
    @GetMapping(value = "/consumer/pay/nacos/get/{OrderNo}")
    public ResultData getPayByOrderNo(@PathVariable("OrderNo") String OrderNo) {
        return payFeignSentinelApi.getPayByOrderNo(OrderNo);
    }

}
