package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.AiOrderDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.AiOrderService;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Agent相关接口")
@Slf4j
@RequestMapping("/user/ai")
public class AiController {

    @Autowired
    private AiOrderService aiOrderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/order")
    @ApiOperation("推荐菜品")
    public Result aiOrder(@RequestBody AiOrderDTO dto) {
        Long userId = BaseContext.getCurrentId();
        List<Dish> dishes = aiOrderService.recommendDishes(dto.getInput(), userId);
        return Result.success(dishes);
    }
}
