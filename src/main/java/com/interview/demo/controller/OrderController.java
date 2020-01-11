package com.interview.demo.controller;

import com.interview.demo.entity.Orde;
import com.interview.demo.service.IOrderService;
import com.interview.demo.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.controller
 * @ClassName: OrderController
 * @Author: Kaiser
 * @Description: OrderController
 * @Date: 2020-01-10 12:53
 * @Version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/addOrder")
    public JsonData addOrder(String number, String name, String price){

        Orde orde = new Orde(null,number,name,price,null);
        boolean b = orderService.save(orde);
        if (b) {
            return new JsonData(null,"添加成功",1,true);
        }
        return new JsonData(null,"添加失败",0,false);
    }
}
