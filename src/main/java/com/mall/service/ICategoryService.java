package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Category;

import java.util.List;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/10 12:44.
 */
public interface ICategoryService {

    ServerResponse addCategory(String name, Integer parentId);

    ServerResponse updateCategoryName(Integer id, String name);

    ServerResponse<List<Category>> getParallelChildrenCategory(Integer id);

    ServerResponse<List<Integer>> getChildrenCategoryRecursive(Integer id);
}
