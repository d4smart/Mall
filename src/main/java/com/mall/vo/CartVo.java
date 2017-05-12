package com.mall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/12 10:49.
 */
public class CartVo {

    private List<CartProductVo> cartProductVos;
    private BigDecimal cateTotalPrice;
    private Boolean allChecked; // 是否都勾选
    private String imageHost;

    public List<CartProductVo> getCartProductVos() {
        return cartProductVos;
    }

    public void setCartProductVos(List<CartProductVo> cartProductVos) {
        this.cartProductVos = cartProductVos;
    }

    public BigDecimal getCateTotalPrice() {
        return cateTotalPrice;
    }

    public void setCateTotalPrice(BigDecimal cateTotalPrice) {
        this.cateTotalPrice = cateTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
