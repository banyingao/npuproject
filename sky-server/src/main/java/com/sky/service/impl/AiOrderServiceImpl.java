package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.AiOrderDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.service.AiOrderService;
import com.sky.utils.AiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AiOrderServiceImpl implements AiOrderService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private AiUtil aiUtil;

    @Override
    public List<Dish> recommendDishes(String input, Long userId) {

        // 1. 调用AI返回菜品ID列表
        List<Long> dishIds = recommendDishIds(input, userId);

        // 2. 如果AI没有返回结果，则返回默认推荐
        if (dishIds == null || dishIds.isEmpty()) {
            log.info("AI未返回结果，使用默认推荐菜品");
            return dishMapper.findTopDishes(5);
        }

        // 3. 查询数据库中对应的菜品信息
        List<Dish> dishes = dishMapper.findByIds(dishIds);

        // 4. 返回完整菜品对象
        return dishes;
    }

    @Override
    public List<Long> recommendDishIds(String input, Long userId) {

        String result = aiUtil.callAI(input);
        log.info("AI原始返回: {}", result);

        List<Long> dishIds = new ArrayList<>();

        try {
            if (result == null || result.isEmpty()) {
                log.error("AI返回结果为空");
                return dishIds;
            }

            JSONObject json = JSON.parseObject(result);

            if (json.getJSONObject("output") == null) {
                log.error("AI返回格式错误：不存在 output 字段");
                return dishIds;
            }

            String text = json.getJSONObject("output").getString("text");
            log.info("AI解析text内容: {}", text);

            if (text == null || text.isEmpty()) {
                log.error("AI返回 text 为空");
                return dishIds;
            }

            JSONObject data = JSON.parseObject(text);
            JSONArray idsArray = data.getJSONArray("dishIds");

            if (idsArray != null) {
                dishIds = idsArray.toJavaList(Long.class);
            }

        } catch (Exception e) {
            log.error("AI解析失败", e);
        }

        log.info("最终返回菜品ID: {}", dishIds);
        return dishIds;
    }

}