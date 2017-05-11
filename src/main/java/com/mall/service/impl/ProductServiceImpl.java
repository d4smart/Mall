package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/10 18:55.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse createOrUpdateProduct(Product product) {
        if(product == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        if(StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImages = product.getSubImages().split(",");
            if(subImages.length > 0) {
                product.setMainImage(subImages[0]);
            }
        }

        if(product.getId() != null) {
            // 更新
            int count = productMapper.updateByPrimaryKey(product);
            if(count > 0) {
                return ServerResponse.createBySuccess("更新商品成功");
            } else {
                return ServerResponse.createBySuccess("更新商品失败");
            }
        } else {
            // 创建
            int count = productMapper.insert(product);
            if(count > 0) {
                return ServerResponse.createBySuccess("新增产品成功");
            } else {
                return ServerResponse.createBySuccess("新增产品失败");
            }
        }
    }

    public ServerResponse<String> setSaleStatus(Integer id, Integer status) {
        if(id == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(id);
        product.setStatus(status);

        int count = productMapper.updateByPrimaryKeySelective(product);
        if(count > 0) {
            return ServerResponse.createBySuccess("修改商品销售状态成功");
        } else {
            return ServerResponse.createByErrorMessage("修改商品销售状态失败");
        }
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer id) {
        if(id == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(id);
        if(product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();

        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        // 配置imageHost，parentCategoryId，createTime，updateTime
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.mall.d4smarter.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null) {
            productDetailVo.setParentCategoryId(0); // 默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectList();

        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product product : products) {
            productListVos.add(assembleProductListVo(product));
        }

        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVos);

        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String name, Integer id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if(StringUtils.isNotBlank(name)) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }

        List<Product> products = productMapper.selectByNameAndId(name, id);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product product : products) {
            productListVos.add(assembleProductListVo(product));
        }

        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVos);

        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<ProductDetailVo> getDetail(Integer id) {
        if(id == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(id);
        if(product == null) {
            return ServerResponse.createByErrorMessage("商品不存在或已被删除");
        }
        if(product.getStatus() != Const.ProductStatus.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已被下架");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIds = new ArrayList<Integer>();
        if(categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVos);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIds = iCategoryService.getChildrenCategoryRecursive(categoryId).getData();
        }
        if(StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        // 排序处理
        if(StringUtils.isNotBlank(orderBy)) {
            if(Const.ProductOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Product> products = productMapper.selectByKeywordAndCategoryIds(StringUtils.isNoneBlank(keyword) ? keyword : null, categoryIds.size() == 0 ? null : categoryIds);

        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product product : products) {
            productListVos.add(assembleProductListVo(product));
        }

        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
