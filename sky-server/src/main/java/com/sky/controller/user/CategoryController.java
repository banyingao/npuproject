package com.sky.controller.user;


import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Slf4j
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    private DishService dishService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List> List(Integer type) {

        List<Category> list = categoryService.list(type);
        log.info("根据类型查询的分类为：{}", list);
        return Result.success(list);
    }

}
