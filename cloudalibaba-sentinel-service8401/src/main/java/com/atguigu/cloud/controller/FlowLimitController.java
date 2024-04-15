package com.atguigu.cloud.controller;

import com.atguigu.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowLimitController {

    @GetMapping(value = "/testA")
    public String testA() {
        return "-------testA";
    }

    @GetMapping(value = "/testB")
    public String testB() {
        return "-------testB";
    }

    @Resource
    private FlowLimitService flowLimitService;
    @GetMapping(value = "/testC")
    public String testC() {
        flowLimitService.common();
        return "-------testC";
    }

    @GetMapping(value = "/testD")
    public String testD() {
        flowLimitService.common();
        return "-------testD";
    }

    // 流控效果------排队等待
    @GetMapping(value = "/testE")
    public String testE() {
        System.out.println(System.currentTimeMillis()+"-------testE，流控效果----排队等待");
        return "-------testE";
    }

    // 新增熔断规则-慢调用比例
    @GetMapping(value = "/testF")
    public String testF() {
        // 暂停一秒线程
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("-------testF，新增熔断规则-慢调用比例");
        return "-------testF， 新增熔断规则-慢调用比例";
    }


    // 新增熔断规则-异常比例
    @GetMapping(value = "/testG")
    public String testG() {
        System.out.println("-------testG，新增熔断规则-异常比例");
        int age = 10/0;
        return "-------testG， 新增熔断规则-异常比例";
    }

    // 新增熔断规则-异常数
    @GetMapping(value = "/testH")
    public String testH() {
        System.out.println("-------testH，新增熔断规则-异常数");
        int age = 10/0;
        return "-------testH， 新增熔断规则-异常数";
    }

}
