package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/10 18:55.
 */
public interface IProductService {

    ServerResponse createOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer id, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer id);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String name, Integer id, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getDetail(Integer id);

    ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
