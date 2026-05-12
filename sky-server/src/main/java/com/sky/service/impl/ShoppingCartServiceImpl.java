package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车中是否有数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList != null && shoppingCartList.size() > 0){
            //如果已经存在了，只需要将数最加一
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
            //如果不存在，需要插入一条购物车数据
            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();
            if(dishId != null){
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(String.valueOf(dish.getImage()));
            }else {
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(String.valueOf(setmeal.getImage()));
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }
    /*
     * 查看购物车
     * */
    @Override
    public List<ShoppingCart> list() {
        List<ShoppingCart> list = new ArrayList<>();
        list = shoppingCartMapper.allList();
        return list;
    }
    /*
     * 删除购物车中一个商品
     * */
    @Override
    public void deleteByDishIdOrSetmealId(ShoppingCartDTO shoppingCartDTO) {
        //判断要删除的数据的数量是否大于1
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList != null && shoppingCartList.size() > 0){
            ShoppingCart cart = shoppingCartList.get(0);
            //判断要删除的数据的数量是否大于1
            if(cart.getNumber() > 1){
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(cart);
            }else{
                //当前商品在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteById(cart.getId());
            }
        }

    }
    /*
     *清空购物车
     * */
    @Override
    public void cleanShoppongCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    public void addByDishId(Long dishId) {
        ShoppingCartDTO dto = new ShoppingCartDTO();
        dto.setDishId(dishId);
        this.addShoppingCart(dto);
    }
}
