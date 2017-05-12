package com.mall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/12 11:08.
 */
public class BigDecimalTest {

    @Test
    public void test1() {
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);
        System.out.println(4.015 * 100);
        System.out.println(123.3 / 100);
    }

    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal("0.01");
        BigDecimal b2 = new BigDecimal("0.05");
        System.out.println(b1.add(b2));
    }
}
