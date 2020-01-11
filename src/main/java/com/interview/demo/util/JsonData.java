package com.interview.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.util
 * @ClassName: JsonData
 * @Author: Kaiser
 * @Description: 结果封装类
 * @Date: 2020-01-10 09:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData {
    private Object data;
    private String msg;
    private int code;
    private boolean success;
}
