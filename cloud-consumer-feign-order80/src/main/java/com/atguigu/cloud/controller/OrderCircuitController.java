package com.atguigu.cloud.controller;

import com.atguigu.cloud.apis.PayFeignApi;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class OrderCircuitController {
    @Resource
    private PayFeignApi payFeignApi;

    //  feign 调用机制
    @GetMapping(value = "/feign/pay/circuit/{id}")
    @CircuitBreaker(name = "cloud-payment-service", fallbackMethod = "myCircuitFallBack")
    public String myCircuit(@PathVariable("id") Integer id) {
        return payFeignApi.myCircuit(id);
    }
    //  myCircuitFallBack--服务降级后的兜底处理方法
    public String myCircuitFallBack(Integer id, Throwable t) {
        //  容错处理逻辑， 返回备用结果
        return "myCircuitFallBack，系统繁忙，请稍后再试~";
    }

    //  舱壁隔离
//    @GetMapping(value = "/feign/pay/bulkhead/{id}")
//    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkheadFallBack", type = Bulkhead.Type.SEMAPHORE)
//    public String myBulkhead(@PathVariable("id") Integer id) {
//        return payFeignApi.myBulkhead(id);
//    }
//    public String myBulkheadFallBack(Integer id, Throwable t) {
//        return "myBulkheadFallBack，系统繁忙，请稍后再试~";
//    }


    // 舱壁隔离 threadpool
    @GetMapping(value = "/feign/pay/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkheadPollFallBack", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> myBulkheadTHREADPOOL(@PathVariable("id") Integer id) {
        System.out.println(Thread.currentThread().getName()+"\t"+"----开始进入");
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"\t"+"----准备离开");

        return CompletableFuture.supplyAsync(() -> payFeignApi.myBulkhead(id) + "\t" + "Bulkhead.Type.THREADPOOL");
    }
    public CompletableFuture<String> myBulkheadPollFallBack(Integer id, Throwable t) {
        return CompletableFuture.supplyAsync(() -> "Bulkhead.Type.THREADPOOL，系统繁忙，请稍后再试~");
    }


    @GetMapping(value = "/feign/pay/ratelimit/{id}")
    @RateLimiter(name = "cloud-payment-service", fallbackMethod = "myRateLimitFallBack")
    public String myRateLimit(@PathVariable("id") Integer id) {
        return payFeignApi.myRateLimit(id);
    }
    public String myRateLimitFallBack(Integer id, Throwable t) {
        return "myRateLimitFallBack，你被限流了，请稍后再试~";
    }



}
