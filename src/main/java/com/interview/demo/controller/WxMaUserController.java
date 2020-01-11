package com.interview.demo.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.interview.demo.util.JsonUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.controller
 * @ClassName: WxMaUserController
 * @Author: Kaiser
 * @Description: WxMaUserController
 * @Date: 2020-01-10 10:31
 * @Version: 1.0
 */
@RestController
@RequestMapping("/wx/user")
public class WxMaUserController {
    @Resource(name = "wxMaService")
    private WxMaService wxMaService;
    @GetMapping("/login")
    public String login(String code){
        if (StringUtils.isBlank(code)) {
            return "empty jscode";
        }
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            return JsonUtil.toJson(session);
        } catch (WxErrorException e) {
            return e.toString();
        }
    }

    /**
     * 获取accessToken
     * @return access_token
     */
    @GetMapping("/getAccessToken")
    public String getAccessToken() {
        try {
            return JsonUtil.toJson(wxMaService.getAccessToken());
        } catch (WxErrorException e) {
            return e.toString();
        }
    }
}
