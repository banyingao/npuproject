package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /*
     * 新增菜品和对应的口味
     *
     * */
    @Override
    @Transactional
    public void saveWithFlavon(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(flavor->{
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
     * 菜品分页查询
     *
     * */

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage( dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page =  dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
    /*
     * 批量删除菜品
     * */

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除-----是否存在起首中的菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            Integer status = dish.getStatus();
            if(status == StatusConstant.ENABLE){
                //菜品在起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否能够删除-----判断当前菜品是否被套餐关联了
        List<Long> setmealIds= setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            //菜品被套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            //删除菜品关联的口味数据
//            dishFlavorMapper.deleteById(id);
//        }
        //
        //删除菜品表中的菜品数据
        dishMapper.deleteByIds(ids);
        //删除菜品关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /*
     * 根据ID查询菜品和对应的口味数据
     * */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询对应的口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        //封装数据
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /*
     * 修改菜品
     * */
    @Override
    public void uodateWithFlavor(DishDTO dishDTO) {
        //修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除对应的所有的的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(flavor->{
                flavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        return dishMapper.list(dish);
    }
    /**
     * 根据分类id查询菜品
     * @param type
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        ArrayList<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            List<DishFlavor> flavors  = dishFlavorMapper.getByDishId(dishVO.getId());
            dishVO.setFlavors(flavors );
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
