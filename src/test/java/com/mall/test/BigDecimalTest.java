package com.mall.test;

import com.google.common.collect.Lists;
import com.mall.util.FTPUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    @Test
    public void test3() throws IOException {
        // File file = new File("C:\\Users\\23037\\IdeaProjects\\mall\\target\\mall\\path", "qr-1491753014256.png");
        File file = new File("C:/Users/23037/IdeaProjects/mall/target/mall/path", "qr-1491753014256.png");
        // C:/Users/23037/IdeaProjects/mall/target/mall/path qr_1491753014256.png
        FTPUtil.uploadFile(Lists.newArrayList(file));
    }
}
