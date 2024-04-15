package com.atguigu.cloud.service;

import feign.Param;

public interface AccountService {

    // 扣减账户余额
    void decrease(@Param("userId") Long userId, @Param("money") Long money);

}
