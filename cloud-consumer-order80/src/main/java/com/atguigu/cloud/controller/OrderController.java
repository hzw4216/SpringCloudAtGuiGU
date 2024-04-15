package com.atguigu.cloud.controller;


import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
//    public static final String PaymentSrl_URL = "http://localhost:8001";  // 先写死，硬编码

    public static final String PaymentSrl_URL = "http://cloud-payment-service";     // 服务注册中心上的微服务名称

    @Resource
    private RestTemplate restTemplate;

    /**
     *  前端客户调用后端接口
     *  PaymentSrl_URL + "/pay/add"：前台本机调用路径+接口路径
     *  payDTO：请求参数
     *  ResultData.class：返回的数据
     */
    @PostMapping(value = "/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrl_URL + "/pay/add", payDTO, ResultData.class);
    }

    @DeleteMapping(value = "/consumer/pay/del/{id}")
    public ResultData deletePayInfo(@PathVariable("id") Integer id) {
        // 执行删除操作
        restTemplate.delete(PaymentSrl_URL + "/pay/del/" + id);

        // 创建并返回一个结果对象
        ResultData result = new ResultData();
        result.setMessage("Delete successful for ID: " + id);
        result.setCode("200"); // 假设你的ResultData类有这样的字段
        return result;
    }

    @PutMapping(value = "/consumer/pay/update")
    public ResultData updateOrder(PayDTO payDTO) {
        restTemplate.put(PaymentSrl_URL + "/pay/update", payDTO, ResultData.class);
        ResultData result = new ResultData();
        result.setMessage("Delete successful");
        result.setCode("200"); // 假设你的ResultData类有这样的字段
        return result;
    }

    @GetMapping(value = "/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrl_URL + "/pay/get/"+id, ResultData.class, id);
    }

    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul() {
        return restTemplate.getForObject(PaymentSrl_URL + "/pay/get/info", String.class);
    }

    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println(service);
        }

        System.out.println("==============");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getServiceId()+ "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }


}
