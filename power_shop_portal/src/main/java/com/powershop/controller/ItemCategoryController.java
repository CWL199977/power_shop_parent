package com.powershop.controller;

import com.powershop.feign.ItemServiceFeign;
import com.powershop.utils.CatResult;
import com.powershop.utils.PageResult;
import com.powershop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll() {
        try {
            CatResult catResult = itemServiceFeign.selectItemCategoryAll();
            return Result.ok(catResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
}
