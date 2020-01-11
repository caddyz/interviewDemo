package com.interview.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.entity
 * @ClassName: Orde
 * @Author: Kaiser
 * @Description: 订单
 * @Date: 2020-01-10 12:48
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orde implements Serializable {
    private static final long serialVersionUID = -877148805021381696L;
    private Integer ordeId;
    private String ordeNo;
    private String ordeName;
    private String ordePrice;
    private String ordeWxNumber;
}
