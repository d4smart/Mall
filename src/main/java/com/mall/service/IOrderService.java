package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.vo.OrderVo;

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

    ServerResponse<String> cancel(Integer userId, Long orderNumber);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNumber);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNumber);

    ServerResponse<PageInfo> manageSearch(Long orderNumber, int pageNum, int pageSize);

    ServerResponse<String> manageSend(Long orderNumber);
}
