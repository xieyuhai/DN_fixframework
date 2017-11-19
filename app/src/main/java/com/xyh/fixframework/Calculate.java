package com.xyh.fixframework;

/**
 * 错误类
 * Created by xieyuhai on 2017/11/16.
 */

public class Calculate {

    public int calc() {

        int i = 0;
        int j = 10;
        //模拟异常产生
        return j / i;
    }


    public int calculate(int i) {

        if (i != 0) {
            throw new ArithmeticException();
        }
        int j = 10;
        //模拟异常产生
        return j / i;
    }
}
