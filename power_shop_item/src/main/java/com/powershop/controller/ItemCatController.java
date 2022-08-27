package com.powershop.controller;


import com.powershop.pojo.TbItemCat;
import com.powershop.service.ItemCatService;
import com.powershop.utils.CatResult;
import com.powershop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 查询商品类名
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0") Long id) {
        try {
            List<TbItemCat> tbItemCatList = itemCatService.selectItemCategoryByParentId(id);
            return Result.ok(tbItemCatList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("没有查询到");
        }
    }
    /**
     * 查询首页商品分类
     */
    @RequestMapping("/selectItemCategoryAll")
    public CatResult selectItemCategoryAll(){
        return itemCatService.selectItemCategoryAll();
    }

}