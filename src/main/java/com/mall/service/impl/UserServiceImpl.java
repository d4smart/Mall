package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/8 21:31.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if(count == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // 密码登陆MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(Const.USERNAME, user.getUsername());
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(Const.EMAIL, user.getEmail());
        if(!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int count = userMapper.insert(user);
        if(count == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String type, String str) {
        if(StringUtils.isNotBlank(type)) {
            // 开始校验
            if(Const.USERNAME.equals(type)) {
                int count = userMapper.checkUsername(str);
                if(count > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            } else if(Const.EMAIL.equals(type)) {
                int count = userMapper.checkEmail(str);
                if(count > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(Const.USERNAME, username);
        if(validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMessage("找回密码问题没有设置");
        }

        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username, question, answer);
        if(count > 0) {
            // 找回密码问题验证成功
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_" + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        } else {
            return ServerResponse.createByErrorMessage("密码提示问题答案错误");
        }
    }
}
