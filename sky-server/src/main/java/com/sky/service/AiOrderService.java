package com.sky.service;

import com.sky.entity.Dish;

import java.util.List;

public interface AiOrderService {

     List<Dish> recommendDishes(String input, Long userId);
     List<Long> recommendDishIds(String input, Long userId);

}
