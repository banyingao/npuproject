package com.sky.task;


import com.sky.context.BaseContext;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 *定时任务类，定时处理订单状态
 */

@Component
@Slf4j
public class OrderTask {

    /**
     *
     *处理超时订单的方法
     */
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理订单状态：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getOrdersByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        if(orders != null && orders.size() > 0){
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消！");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
    public void processDeliveryOrder(){
        log.info("及时更新派送中的订单状态：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> orders = orderMapper.getOrdersByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        if(orders != null && orders.size() > 0){
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
