package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/8 20:18.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String type, String str);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

}
