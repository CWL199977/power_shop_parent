package com.powershop.controller;


import com.powershop.pojo.TbItem;
import com.powershop.service.ItemService;
import com.powershop.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.powershop.utils.Result;

import java.util.Map;

@RestController
@RequestMapping("/backend/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 分页查询商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer rows) {
        try {
            PageResult pageResult = itemService.selectTbItemAllByPage(page, rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 商品的添加
     *
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem, String desc, String itemParams) {
        try {
            itemService.insertTbItem(tbItem, desc, itemParams);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    /**
     * 回显商品信息
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(Long itemId) {
        try {
            Map<String, Object> map = itemService.preUpdateItem(itemId);
            return Result.ok(map);
        } catch (Exception e) {
            return Result.error("回显失败");
        }
    }

    /**
     * 修改商品数据信息
     *
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/updateTbItem")
    public Result updateTbItem(TbItem tbItem, String desc, String itemParams) {
        try {
            itemService.updateTbItem(tbItem, desc, itemParams);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("修改失败");
        }
    }

    /**
     * 删除模板信息
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/deleteItemById")
    public Result deleteItemParamById(Long itemId) {
        try {
            itemService.deleteItemById(itemId);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }

}