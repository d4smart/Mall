package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/10 12:45.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String name, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(name)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setStatus(true); // 分类可用

        int count = categoryMapper.insert(category);
        if(count > 0) {
            return ServerResponse.createBySuccess("添加分类成功");
        } else {
            return ServerResponse.createByErrorMessage("添加分类失败");
        }
    }

    public ServerResponse updateCategoryName(Integer id, String name) {
        if(id == null || StringUtils.isBlank(name)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setId(id);
        category.setName(name);

        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if(count > 0) {
            return ServerResponse.createBySuccess("更新成功");
        } else {
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    public ServerResponse<List<Category>> getParallelChildrenCategory(Integer id) {
        List<Category> categories = categoryMapper.selectChildrenCategoryById(id);
        if(CollectionUtils.isEmpty(categories)) {
            logger.info("未找到当前分类的子分类");
        }

        return ServerResponse.createBySuccess(categories);
    }

    /**
     * 递归查询这个分类和所有分类的id
     * @param integer id
     * @return List
     */
    public ServerResponse<List<Integer>> getChildrenCategoryRecursive(Integer id) {
        Set<Category> categories = Sets.newHashSet();
        findChildCategory(categories, id);

        List<Integer> list = Lists.newArrayList();
        if(id != null) {
            for(Category category : categories) {
                list.add(category.getId());
            }
        }

        return ServerResponse.createBySuccess(list);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if(category != null) {
            categorySet.add(category);
        }

        List<Category> categories = categoryMapper.selectChildrenCategoryById(id);
        for(Category categoryItem : categories) {
            findChildCategory(categorySet, categoryItem.getId());
        }

        return categorySet;
    }
}
