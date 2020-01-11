package com.interview.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.interview.demo.entity.User;
import com.interview.demo.mapper.UserMapper;
import com.interview.demo.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.service.impl
 * @ClassName: UserServiceImpl
 * @Author: Kaiser
 * @Description:
 * @Date: 2020-01-10 09:48
 * @Version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
