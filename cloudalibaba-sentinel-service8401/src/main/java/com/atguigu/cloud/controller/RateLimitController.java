package com.atguigu.cloud.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController {
    @GetMapping(value = "/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试";
    }

    @GetMapping(value = "/rateLimit/byResource")
    @SentinelResource(value = "byResourceSentinelResource", blockHandler = "handleException")
    public String byResource() {
        return "按照资源名称限流测试";
    }
    public String handleException(BlockException blockException) {   //添加BlockException blockException 来实现自定义错误处理
        return "服务不可用，触发了SentinelResource";
    }

    @GetMapping(value = "/rateLimit/doAction/{p1}")
    // blockHandler：主要针对sentinel配置后出现的违规异常
    // fallback：程序异常了JVM抛出的异常服务降级
    @SentinelResource(value = "doActionSentinelResource", blockHandler = "doActionBlockHandler", fallback="doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0) {
            throw new RuntimeException("p1=1+参数异常");
        }
        return "doAction";
    }
    public String doActionBlockHandler(@PathVariable("p1") Integer p1, BlockException e) {
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel配置自定义限流了";
    }

    public String doActionFallback(@PathVariable("p1") Integer p1, Throwable e) {
        log.error("程序逻辑异常了：{}", e);
        return "程序逻辑异常了"+"\t"+e.getMessage();
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value="testHotKey", blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value="p1", required=false) String p1,
                             @RequestParam(value="p2", required=false) String p2) {
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1, String p2, BlockException e) {
        return "------dealHandler_testHotKey";
    }

}
