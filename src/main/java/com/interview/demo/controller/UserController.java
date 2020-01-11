package com.interview.demo.controller;

import com.interview.demo.entity.User;
import com.interview.demo.service.IUserService;
import com.interview.demo.util.JsonData;
import com.interview.demo.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.controller
 * @ClassName: UserController
 * @Author: Kaiser
 * @Description: user控制类
 * @Date: 2020-01-10 09:49
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/addUser")
    public JsonData addUser(String name){
        User user = new User(null,name,null,null,null,null,null,null);
//        user.setUserId(null);
//        user.setUserName(name);
        boolean b = userService.save(user);
        if (b) {
            return new JsonData(null,"添加成功",1,true);
        }
        return new JsonData(null,"添加失败",0,false);
    }

    @GetMapping("/firstAdd")
    public JsonData firstAdd(String openid, String phone, @RequestParam(required = false) String superior){
        User user =  new User(null,null,null,phone,superior, TimeUtil.getNowTime(),TimeUtil.getNowTime(),openid);
        boolean b = userService.save(user);
        if (b) {
            return new JsonData(null,"添加成功",1,true);
        }
        return new JsonData(null,"添加失败",0,false);
    }
}
