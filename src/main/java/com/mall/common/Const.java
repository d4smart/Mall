package com.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/8 20:14.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface CartStatus {
        int CHECKED = 1; // 购物车选中
        int UNCHECKED = 0; // 购物车未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }

    public enum ProductStatus {
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatus(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
