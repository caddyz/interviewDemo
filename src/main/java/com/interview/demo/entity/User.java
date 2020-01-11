package com.interview.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.entity
 * @ClassName: User
 * @Author: Kaiser
 * @Description: User实体
 * @Date: 2020-01-10 09:29
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 6213878459594739897L;
    private Integer userId;
    private String userName;
    private String userAvatar;
    private String userPhone;
    private String userSuperior;
    private String loginTime;
    private String getTime;
    private String userOpenid;
}
