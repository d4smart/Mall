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
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        } else {
            return ServerResponse.createByErrorMessage("密码提示问题答案错误");
        }
    }

    public ServerResponse<String> forgetResetPassword(String username, String forgetToken, String newPassword) {
        if(StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("token不能为空");
        }
        ServerResponse validResponse = this.checkValid(Const.USERNAME, username);
        if(validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            } else {
                return ServerResponse.createByErrorMessage("修改密码失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("token不匹配，请重新获取");
        }
    }

    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {
        // 防止横向越权
        int count = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(oldPassword));
        if(count == 0) {
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        } else {
            return ServerResponse.createByErrorMessage("密码更新失败");
        }
    }

    public ServerResponse<User> updateUserInfo(User user) {
        // username不能被更新，email要进行校验
        int count = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if(count > 0) {
            return ServerResponse.createByErrorMessage("email已被占用");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        } else {
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }
    }

    public ServerResponse<User> getUserInfo(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }

    /*
      backend
     */

    /**
     * 校验用户是否是管理员
     * @param User user
     * @return ServerResponse
     */
    public ServerResponse isAdmin(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
