package com.interview.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.properties
 * @ClassName: WxMaProperties
 * @Author: Kaiser
 * @Description: 小程序配置类
 * @Date: 2020-01-10 10:19
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
