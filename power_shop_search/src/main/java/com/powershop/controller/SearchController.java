package com.powershop.controller;

import com.powershop.pojo.SearchItem;
import com.powershop.service.SearchService;
import com.powershop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/importAll")
    public Result importAll() {
        try {
            searchService.importAll();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("导入失败");
        }
    }

    /**
     * 商品搜索
     *
     * @param q
     * @return
     */
    @RequestMapping("/list")
    public List<SearchItem> list(String q,
                                 @RequestParam(defaultValue = "1") Long page,
                                 @RequestParam(defaultValue = "20") Integer pageSize) {
        return searchService.list(q, page, pageSize);
    }
}