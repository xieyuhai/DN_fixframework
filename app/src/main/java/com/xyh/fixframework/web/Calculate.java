package com.xyh.fixframework.web;

import com.xyh.fixframework.Replace;

/**
 * 修复错误类
 * Created by xieyuhai on 2017/11/16.
 */

public class Calculate {

    @Replace(clazz = "com.xyh.fixframework.Calculate", method = "calc")
    public int calc() {

        //10/0
        int i = 1;
        int j = 10;
        //模拟异常产生
        return j / i;
    }

    @Replace(clazz = "com.xyh.fixframework.Calculate", method = "calculate")
    public int calculate(int i) {
        //10/0
        int j = 10;
        //模拟异常产生
        return j / i;
    }
}
