package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/10 12:24.
 */
@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String name, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        // 校验用户是否是管理员
        if(iUserService.isAdmin(user).isSuccess()) {
            // 添加分类
            return iCategoryService.addCategory(name, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "update_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer id, String name) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        // 校验用户是否是管理员
        if(iUserService.isAdmin(user).isSuccess()) {
            // 更新分类的名称
            return iCategoryService.updateCategoryName(id, name);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "get_children_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getParallelChildrenCategory(HttpSession session, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        // 校验用户是否是管理员
        if(iUserService.isAdmin(user).isSuccess()) {
            // 获取一级子节点
            return iCategoryService.getParallelChildrenCategory(id);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "get_children_category_recursive.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenCategoryRecursive(HttpSession session, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        // 校验用户是否是管理员
        if(iUserService.isAdmin(user).isSuccess()) {
            // 递归获取所有子节点
            return iCategoryService.getChildrenCategoryRecursive(id);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}
