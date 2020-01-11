package com.interview.demo.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.interview.demo.properties.WxMaProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.config
 * @ClassName: WxMaConfiguration
 * @Author: Kaiser
 * @Description: WxMaConfiguration配置
 * @Date: 2020-01-10 10:22
 * @Version: 1.0
 */
@AllArgsConstructor
@Configuration
@ConditionalOnClass(WxMaService.class)
@EnableConfigurationProperties(WxMaProperties.class)
@ConditionalOnProperty(prefix = "wx.miniapp", value = "enabled", matchIfMissing = true)
public class WxMaConfiguration {
    private WxMaProperties properties;
    /**
     * 小程序service.
     *
     * @return 小程序service
     */
    @Bean(name = "wxMaService")
//    @ConditionalOnMissingBean(WxMaService.class)
    public WxMaService service() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(StringUtils.trimToNull(this.properties.getAppid()));
        config.setSecret(StringUtils.trimToNull(this.properties.getSecret()));
        config.setMsgDataFormat(StringUtils.trimToNull(this.properties.getMsgDataFormat()));
        final WxMaServiceImpl service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}
