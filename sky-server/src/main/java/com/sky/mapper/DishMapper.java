package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 根据分类id查询菜品数量
 * @param categoryId
 * @return
 */
@Mapper
public interface DishMapper {


    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(long categoryId);

    /*
    * 插入菜品数据
    * */
    @AutoFill(value = OperationType.INSERT)
     void insert(Dish dish);

    /*
    * 菜品分页查询
    * */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
    * 根据主键查询菜品
    * */
    @Select("select *from dish where id = #{id}")
    Dish getById(Long id);

    /*
    * 根据主键删除菜品
    * */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /*
    * 根据菜品ID集合批量删除
    * */
    void deleteByIds(List<Long> ids);

    //根据id动态修改菜品信息
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select *from dish d left join setmeal_dish s on d.id = s.dish_id where s.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
    /**
     * 根据分类id查询菜品
     * @param type
     * @return
     */
    List<DishVO> getlistWithFlavor(Dish dish);


    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish findById(@Param("id") Long id);

    @Select("<script>" +
            "SELECT * FROM dish WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<Dish> findByIds(@Param("ids") List<Long> ids);

    @Select("SELECT * FROM dish ORDER BY create_time DESC LIMIT #{limit}")
    List<Dish> findTopDishes(@Param("limit") int limit);


}
