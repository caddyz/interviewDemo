package com.interview.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.interview.demo.entity.Orde;
import com.interview.demo.mapper.OrderMapper;
import com.interview.demo.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.service.impl
 * @ClassName: OrderServiceImpl
 * @Author: Kaiser
 * @Description:
 * @Date: 2020-01-10 12:52
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orde> implements IOrderService {
}
