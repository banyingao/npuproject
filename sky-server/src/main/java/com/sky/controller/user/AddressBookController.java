package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "C端用户地址相关接口")
@Slf4j
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;


    /*
    * 查询当前登录用户的所有地址信息
    * */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> selectAllAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.selectAllAddressBook(addressBook);
        return Result.success(list);
    }
    /*
    * 新增地址
    *
    * */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.saveAddressBook(addressBook);
        return Result.success();
    }
    /*
     *
     * 根据id查询地址
     * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
    /*根据id修改地址
    */

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }
    /*
    * 设置默认地址
    * */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
    /*
    * 根据id删除地址
    * */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result delete(Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }
    /*
    * 查询默认地址
    * */
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        //判断是否存在默认地址
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        List<AddressBook> list = addressBookService.selectAllAddressBook(addressBook);
        if(list != null && list.size() == 1){
            return Result.success(list.get(0));
        }
        log.info("没有查询到默认地址");
        return Result.error("没有查询到默认地址");
    }
}
