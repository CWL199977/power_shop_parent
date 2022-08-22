package com.powershop.service;

import com.powershop.pojo.TbItemParam;
import com.powershop.utils.PageResult;

public interface ItemParamService {
    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    PageResult selectItemParamAll(Integer page, Integer rows);

    void insertItemParam(Long itemCatId, String paramData);

    void deleteItemParamById(Long id);
}
