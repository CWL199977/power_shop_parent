package com.powershop.controller;

import com.powershop.feign.ContentServiceFeign;
import com.powershop.utils.AdNode;
import com.powershop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/content")
public class ContentController {

    @Autowired
    private ContentServiceFeign contentServiceFeign;

    /**
     * 查询内容类别
     * @return
     */
    @RequestMapping("/selectFrontendContentByAD")
    public Result selectFrontendContentByAD(){
        try {
            List<AdNode> adNodeList = contentServiceFeign.selectFrontendContentByAD();
            return Result.ok(adNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
}
