package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public interface ShoppingCartService {

    /*
     * 添加购物车
     * */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    /*
     * 查看购物车
     * */
    List<ShoppingCart> list();
    /*
     * 删除购物车中一个商品
     * */
    void deleteByDishIdOrSetmealId(ShoppingCartDTO shoppingCartDTO);
    /*
     *清空购物车
     * */
    void cleanShoppongCart();
    /*
    * AI调用
    * */
    void addByDishId(Long dishId);

}
