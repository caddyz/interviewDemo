package com.interview.demo.util;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.util
 * @ClassName: Constant
 * @Author: Kaiser
 * @Description: 常量类
 * @Date: 2020-01-10 10:43
 * @Version: 1.0
 */
public class Constant {
    /**
     * 支付回调地址
     */
    public static final String NOTIFY_URL = "https://miniapp.miaoyidj.com/api/pay/notify/order";
//    public static final String NOTIFY_URL = "http://7n27sw.natappfree.cc/api/pay/notify/order";

    /**
     * 退款回调地址
     */
    public static final String NOTIFY_REFUND_URL = "https://miniapp.miaoyidj.com/api/pay/notify/refund";

    /**
     *  交易类型
     */
    public static final String TRADE_TYPE = "JSAPI";
}
