package com.atguigu.cloud.controller;

import cn.hutool.core.util.IdUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;


@RestController
public class PayGateWayController {

    @Resource
    PayService payService;

    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo() {
        return ResultData.success("gateway info test: "+ IdUtil.simpleUUID());
    }

    @GetMapping("/pay/gateway/filter")
    public ResultData<String> getFilter(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("请求头: "+headerName+"\t\t\t"+"请求头值"+headerValue);
            if (headerName.equalsIgnoreCase("X-Request-atguigu1")||headerName.equalsIgnoreCase("X-Request-atguigu2")) {
                result.append(headerName).append("\t").append(headerValue);
            }
        }

        System.out.println("===============");
        String customerId = request.getParameter("customerId");
        System.out.println("requset parameter customerId is : " + customerId);

        String customerName = request.getParameter("customerName");
        System.out.println("requset parameter customerName is : " + customerName);
        System.out.println("================");

        return ResultData.success("getGateWayFilter 过滤器 ："+result + "\t ID: " + IdUtil.simpleUUID());
    }

}
