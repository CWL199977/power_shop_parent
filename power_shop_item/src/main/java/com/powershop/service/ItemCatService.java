package com.powershop.service;

import com.powershop.pojo.TbItemCat;

import java.util.List;

public interface ItemCatService {
    List<TbItemCat> selectItemCategoryByParentId(Long id);
}
