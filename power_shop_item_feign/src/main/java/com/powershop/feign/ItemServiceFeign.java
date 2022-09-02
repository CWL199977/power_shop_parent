package com.powershop.feign;

import com.powershop.pojo.SearchItem;
import com.powershop.utils.CatResult;
import com.powershop.pojo.SearchItem;
import com.powershop.utils.CatResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("power-shop-item")
public interface ItemServiceFeign {

    @RequestMapping("/backend/itemCategory/selectItemCategoryAll")
    public CatResult selectItemCategoryAll();

    @RequestMapping("/backend/item/selectSearchItem")
    List<SearchItem> selectSearchItem(@RequestParam("page") int page, @RequestParam("rows") int rows);
}

