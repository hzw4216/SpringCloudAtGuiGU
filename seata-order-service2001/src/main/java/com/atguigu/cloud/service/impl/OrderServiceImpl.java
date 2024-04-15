package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AccountFeignApi accountFeignApi;

    @Resource   // 订单微服务通过 AccountFeignApi 调用库存微服务
    private StorageFeignApi storageFeignApi;

    @Override
    @GlobalTransactional(name= "zzyy-create-order", rollbackFor=Exception.class)
    public void create(Order order) {

        // xid 全局事务id检查
        String xid = RootContext.getXID();
        // 新建订单
        log.info("----------开始新建订单"+"\t"+"xid: "+xid);
        order.setStatus(0);
        // 插入订单成功后，获得实体对象
        int result = orderMapper.insertSelective(order);
        Order orderFromDB = null;
        if(result > 0) {
            // 从 musql 中查询刚插入的数据
            orderFromDB = orderMapper.selectOne(order);
            log.info("新建订单成功");
            System.out.println();
            // 扣减库存
            storageFeignApi.decrease(orderFromDB.getProductId(), orderFromDB.getCount());
            log.info("扣减库存成功");
            System.out.println();
            // 扣减账户
            accountFeignApi.decrease(orderFromDB.getUserId(), orderFromDB.getMoney());
            log.info("扣减账户余额成功");
            System.out.println();
            // 修改订单状态
            orderFromDB.setStatus(1);

            Example whereCondition = new Example(Order.class);
            Example.Criteria criteria = whereCondition.createCriteria();
            criteria.andEqualTo("userId", orderFromDB.getUserId());
            criteria.andEqualTo("status", 0);

            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, whereCondition);
            log.info("修改订单状态成功");
        }
        System.out.println();
        log.info("-----------结束新建订单："+"\t"+"xid: "+xid);

    }
}
