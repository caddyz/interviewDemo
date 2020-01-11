package com.interview.demo.util;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.util
 * @ClassName: TimeUtil
 * @Author: Kaiser
 * @Description: TimeUtil
 * @Date: 2020-01-10 10:58
 * @Version: 1.0
 */
public class TimeUtil {
    public static long getSignTimeStmap(){
        return System.currentTimeMillis() / 1000;
    }

    public static String getNowTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}
