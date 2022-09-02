package com.powershop.service;

import com.powershop.pojo.SearchItem;
import com.powershop.pojo.TbItem;
import com.powershop.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface ItemService {

    PageResult selectTbItemAllByPage(Integer page, Integer rows);


    void insertTbItem(TbItem tbItem, String desc, String itemParams);

    Map<String, Object> preUpdateItem(Long itemId);

    void updateTbItem(TbItem tbItem, String desc, String itemParams);

    void deleteItemById(Long itemId);

    List<SearchItem> selectSearchItem(int page, int rows);
}
