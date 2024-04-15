package com.atguigu.cloud.controller;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.AccountService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("/account/decrease")
    public ResultData decrease(@Param("userId") Long userId, @Param("money") Long money) {
        accountService.decrease(userId, money);
        return ResultData.success("扣减账户余额成功");
    }

}
