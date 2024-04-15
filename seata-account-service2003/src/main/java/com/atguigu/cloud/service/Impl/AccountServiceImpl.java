package com.atguigu.cloud.service.Impl;

import com.atguigu.cloud.entities.Account;
import com.atguigu.cloud.mapper.AccountMapper;
import com.atguigu.cloud.service.AccountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public void decrease(Long userId, Long money) {

        log.info("开始扣减账户余额");
        accountMapper.decrease(userId, money);
        log.info("扣减账户余额成功");

        myTimeout();


    }

    // 模拟超时异常，全局事务回滚
    private void myTimeout() {
        try {
            TimeUnit.SECONDS.sleep(65);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
