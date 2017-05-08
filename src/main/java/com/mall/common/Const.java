package com.mall.common;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/8 20:14.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }
}
