package com.mall.service;

import com.mall.common.ServerResponse;

import java.util.Map;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/17 14:55.
 */
public interface IOrderService {

    ServerResponse pay(Long orderNumber, Integer userId, String path);

    ServerResponse alipayCallBack(Map<String, String> params);

    ServerResponse getPayStatus(Integer userId, Long orderNumber);

    ServerResponse createOrder(Integer userId, Integer shippingId);
}
