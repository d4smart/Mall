package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/8 20:17.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
     * @param username 用户名
     * @param password 密码
     * @param session 用户的session
     * @return ServerResponse
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        // service -> mybatis -> dao
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String type, String str) {
        return iUserService.checkValid(type, str);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String token, String newPassword) {
        return iUserService.forgetResetPassword(username, token, newPassword);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("你还没有登陆");
        }

        return iUserService.resetPassword(user, oldPassword, newPassword);
    }

    @RequestMapping(value = "update_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null) {
            return ServerResponse.createByErrorMessage("你还没有登陆");
        }

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    @RequestMapping(value = "get_user_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserDetail(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户没有登陆，需要强制登陆");
        }

        return iUserService.getUserInfo(user.getId());
    }
}
