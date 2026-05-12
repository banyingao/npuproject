package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /*
     * 向口味表中批量插入数据
     * */
    @AutoFill(value = OperationType.INSERT)
    void insertBatch(List<DishFlavor> flavors);


    //根据dish_id删除口味数据
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
    /*
    * 根据菜品ID集合批量删除口味数据
    * */
    void deleteByDishIds(List<Long> dishIds);
    
    /*
    * 根据菜品id查询口味数据
    * */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
