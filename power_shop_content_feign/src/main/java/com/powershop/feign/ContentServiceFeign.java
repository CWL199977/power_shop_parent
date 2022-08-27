package com.powershop.feign;

import com.powershop.utils.AdNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient("power-shop-content")
@RequestMapping("/backend/content")
public interface ContentServiceFeign {
    @RequestMapping("/selectFrontendContentByAD")
    List<AdNode> selectFrontendContentByAD();
}
