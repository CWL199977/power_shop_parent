package com.powershop.feign;

import com.powershop.utils.CatResult;
import com.powershop.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;;
@FeignClient("power-shop-item")
@RequestMapping("/backend/itemCategory")
public interface ItemServiceFeign {

    @RequestMapping("/selectItemCategoryAll")
    CatResult selectItemCategoryAll();
}
