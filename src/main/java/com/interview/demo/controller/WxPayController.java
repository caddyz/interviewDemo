package com.interview.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.wxpay.sdk.WXPayUtil;
import com.interview.demo.util.Constant;
import com.interview.demo.util.JsonData;
import com.interview.demo.util.NetworkInterfaceUtil;
import com.interview.demo.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.controller
 * @ClassName: WxPayController
 * @Author: Kaiser
 * @Description:
 * @Date: 2020-01-10 10:37
 * @Version: 1.0
 */
@RestController
@RequestMapping("/pay")
public class WxPayController {
    private WxPayService wxService;

    private static final Logger logger = LoggerFactory.getLogger(WxPayController.class);
    /**
     *  使用锁实现独占访问   使用公平锁  先来先得
     */
    private final ReentrantLock lock = new ReentrantLock(true);

    @Autowired
    public WxPayController(WxPayService wxService) {
        this.wxService = wxService;
    }

    /**
     * <pre>
     * 查询订单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2)
     * 该接口提供所有微信支付订单的查询，商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况：
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
     * ◆ 调用被扫支付API，返回USERPAYING的状态；
     * ◆ 调用关单或撤销接口API之前，需确认支付状态；
     * 接口地址：https://api.mch.weixin.qq.com/pay/orderquery
     * </pre>
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     */
    @GetMapping("/queryOrder")
    public WxPayOrderQueryResult queryOrder(@RequestParam(required = false) String transactionId,
                                            @RequestParam(required = false) String outTradeNo)
            throws WxPayException {
        return this.wxService.queryOrder(transactionId, outTradeNo);
    }

    @PostMapping("/queryOrder")
    public WxPayOrderQueryResult queryOrder(@RequestBody WxPayOrderQueryRequest wxPayOrderQueryRequest) throws WxPayException {
        return this.wxService.queryOrder(wxPayOrderQueryRequest);
    }

    /**
     * <pre>
     * 关闭订单
     * 应用场景
     * 以下情况需要调用关单接口：
     * 1. 商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2. 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * 注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
     * 接口地址：https://api.mch.weixin.qq.com/pay/closeorder
     * 是否需要证书：   不需要。
     * </pre>
     *
     * @param outTradeNo 商户系统内部的订单号
     */
    @GetMapping("/closeOrder/{outTradeNo}")
    public WxPayOrderCloseResult closeOrder(@PathVariable String outTradeNo) throws WxPayException {
        return this.wxService.closeOrder(outTradeNo);
    }

    @PostMapping("/closeOrder")
    public WxPayOrderCloseResult closeOrder(@RequestBody WxPayOrderCloseRequest wxPayOrderCloseRequest) throws WxPayException {
        return this.wxService.closeOrder(wxPayOrderCloseRequest);
    }

    /**
     * 调用统一下单接口，并组装生成支付所需参数对象.
     *
     * @param request 统一下单请求参数
     * @param <T>     请使用{@link com.github.binarywang.wxpay.bean.order}包下的类
     * @return 返回 {@link com.github.binarywang.wxpay.bean.order}包下的类对象
     */
    @PostMapping("/createOrder")
    public <T> T createOrder(@RequestBody WxPayUnifiedOrderRequest request) throws WxPayException {
        return this.wxService.createOrder(request);
    }

    /**
     * 统一下单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1)
     * 在发起微信支付前，需要调用统一下单接口，获取"预支付交易会话标识"
     * 接口地址：https://api.mch.weixin.qq.com/pay/unifiedorder
     *
     * @param body 请求对象，注意一些参数如appid、mchid等不用设置，方法内会自动从配置对象中获取到（前提是对应配置中已经设置）
     */
    @PostMapping("/unifiedOrder")
    public WxPayUnifiedOrderResult unifiedOrder(@RequestBody JSONObject body) throws WxPayException {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setNotifyUrl(Constant.NOTIFY_URL);
        request.setTradeType(Constant.TRADE_TYPE);
        request.setOpenid(body.getString("openid"));
        request.setAttach(body.getString("attach"));
        request.setBody(body.getString("bodyInfo"));
        request.setOutTradeNo(body.getString("outTradeNo"));
        request.setTotalFee(BaseWxPayRequest.yuanToFen(body.getString("totalFee")));
        request.setSpbillCreateIp(NetworkInterfaceUtil.getMyIp());
        return this.wxService.unifiedOrder(request);
    }

    /**
     * <pre>
     * 微信支付-申请退款
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     * 接口链接：https://api.mch.weixin.qq.com/secapi/pay/refund
     * </pre>
     *
     * @param request 请求对象
     *        参数：transactionId：微信生成的订单号，在支付通知中有返回 outTradeNo:商户系统内部订单号(二选一)
     *                outRefundNo:商户系统内部的退款单号
     *                totalFee：订单总金额，单位为分
     *                refundFee：退款总金额，订单总金额，单位为分
     *                refundDesc：退款原因
     * @return 退款操作结果
     */
    @PostMapping("/refund")
    public WxPayRefundResult refund(@RequestBody WxPayRefundRequest request) {
        try {
            Thread.sleep(1000 * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
        try {
            request.setNotifyUrl(Constant.NOTIFY_REFUND_URL);
            return this.wxService.refund(request);
        } catch (WxPayException e) {
            logger.error("申请退款错误信息{}",e.getMessage());
            WxPayRefundResult result = new WxPayRefundResult();
            result.setErrCodeDes("申请退款出现异常");
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * <pre>
     * 微信支付-查询退款
     * 应用场景：
     *  提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，
     *  银行卡支付的退款3个工作日后重新查询退款状态。
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_5
     * 接口链接：https://api.mch.weixin.qq.com/pay/refundquery
     * </pre>
     * 以下四个参数四选一
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refundId      微信退款单号
     * @return 退款信息
     */
    @GetMapping("/refundQuery")
    public WxPayRefundQueryResult refundQuery(@RequestParam(required = false) String transactionId,
                                              @RequestParam(required = false) String outTradeNo,
                                              @RequestParam(required = false) String outRefundNo,
                                              @RequestParam(required = false) String refundId)
            throws WxPayException {
        return this.wxService.refundQuery(transactionId, outTradeNo, outRefundNo, refundId);
    }

    @PostMapping("/refundQuery")
    public WxPayRefundQueryResult refundQuery(@RequestBody WxPayRefundQueryRequest wxPayRefundQueryRequest) throws WxPayException {
        return this.wxService.refundQuery(wxPayRefundQueryRequest);
    }

    /**
     * 支付回调通知处理
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @PostMapping("/notify/order")
    @Transactional(rollbackFor = Exception.class)
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        // TODO 根据自己业务场景需要构造返回对象
        logger.info("支付回调开始。。。。。。。。。");
        String attach = notifyResult.getAttach();
        String tradeNo = notifyResult.getOutTradeNo();
        return WxPayNotifyResponse.success("成功");
    }

    /**
     * 退款回调通知处理
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @PostMapping("/notify/refund")
    @Transactional(rollbackFor = Exception.class)
    public String parseRefundNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayRefundNotifyResult result = this.wxService.parseRefundNotifyResult(xmlData);
        // TODO 根据自己业务场景需要构造返回对象
        // 获取订单号
        String tradeNo = result.getReqInfo().getOutTradeNo();
        try {
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            logger.error("退款回调错误信息{}",e.getMessage());
            return WxPayNotifyResponse.fail("失败");
        }
    }

    @GetMapping("/secondSign")
    public JsonData sencodeSign(String prepayId) throws Exception {
        WxPayConfig config = wxService.getConfig();
        String nonceStr = WXPayUtil.generateNonceStr();
        String signTime = String.valueOf(TimeUtil.getSignTimeStmap());
        Map<String,String> map = new HashMap<>(5);
        map.put("appId",config.getAppId());
        map.put("timeStamp",signTime);
        map.put("nonceStr",nonceStr);
        map.put("package",prepayId);
        map.put("signType","MD5");
        String signature = WXPayUtil.generateSignature(map, config.getMchKey());
        Map<String,String> data = new HashMap<>(3);
        data.put("str",nonceStr);
        data.put("signTime", signTime);
        data.put("signature",signature);
        return new JsonData(data,"二次签名",1,true);
    }

    /**
     * <pre>
     * 企业付款业务是基于微信支付商户平台的资金管理能力，为了协助商户方便地实现企业向个人付款，针对部分有开发能力的商户，提供通过API完成企业付款的功能。
     * 比如目前的保险行业向客户退保、给付、理赔。
     * 企业付款将使用商户的可用余额，需确保可用余额充足。查看可用余额、充值、提现请登录商户平台“资金管理”https://pay.weixin.qq.com/进行操作。
     * 注意：与商户微信支付收款资金并非同一账户，需要单独充值。
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
     * 接口链接：https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers
     * </pre>
     *  企业付款到零钱
     * @param request 请求对象
     *    request参数构造：partnerTradeNo：商户订单号  openid：用户openid
     *    check_name： 校验用户姓名选项 两个值：NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
     *    reUserName： 当check_name为FORCE_CHECK必传
     *    amount：企业付款金额，单位为分
     *    description：企业付款备注  spbillCreateIp：ip地址
     */
    @PostMapping("/entPay")
    @Transactional(rollbackFor = Exception.class)
    public JsonData entPay(@RequestBody EntPayRequest request) {
        return null;
    }


    /**
     * <pre>
     * 查询企业付款API
     * 用于商户的企业付款操作进行结果查询，返回付款操作详细结果。
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3
     * 接口链接：https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo
     * </pre>
     * 查询企业付款到零钱的结果
     * @param partnerTradeNo 商户订单号
     */
    @GetMapping("/queryEntPay/{partnerTradeNo}")
    public EntPayQueryResult queryEntPay(@PathVariable String partnerTradeNo) throws WxPayException {
        return this.wxService.getEntPayService().queryEntPay(partnerTradeNo);
    }

//    public EntPayResult userRefund(EntPayRequest request){
//        return this.wxEntpay(request,Constant.USER_MCHAPPID);
//    }


//    private EntPayResult wxEntpay(@NotNull EntPayRequest request, String appid){
//        // 开启锁
//        lock.lock();
//        try {
//            request.setMchAppid(appid);
//            request.setSpbillCreateIp(NetworkInterfaceUtil.getMyIp());
//            request.setCheckName("NO_CHECK");
//            return this.wxService.getEntPayService().entPay(request);
//        } catch (WxPayException e) {
//            logger.error("企业付款错误信息{}",e.getMessage());
//            EntPayResult payResult = new EntPayResult();
//            payResult.setErrCodeDes("企业付款异常");
//            return payResult;
//        } finally {
//            // 释放锁
//            lock.unlock();
//        }
//    }
}
