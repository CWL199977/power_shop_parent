package com.powershop.controller;


import com.powershop.pojo.TbItemParam;
import com.powershop.service.ItemParamService;
import com.powershop.utils.PageResult;
import com.powershop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    /**
     * 查询商品规格参数模板
     *
     * @param itemCatId
     * @return
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {
        try {
            TbItemParam tbItemParam = itemParamService.selectItemParamByItemCatId(itemCatId);
            return Result.ok(tbItemParam);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 规格模板查询
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "3") Integer rows) {
        try {
            PageResult pageResult = itemParamService.selectItemParamAll(page, rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 添加模板
     *
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/insertItemParam")
    public Result insertItemParam(Long itemCatId, String paramData) {
        try {
            itemParamService.insertItemParam(itemCatId, paramData);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    /**
     * 删除模板信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteItemParamById")
    public Result deleteItemParamById(Long id) {
        try {
            itemParamService.deleteItemParamById(id);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }


}