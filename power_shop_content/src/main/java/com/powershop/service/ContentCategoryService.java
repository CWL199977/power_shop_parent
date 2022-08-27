package com.powershop.service;

import com.powershop.pojo.TbContent;
import com.powershop.pojo.TbContentCategory;
import com.powershop.utils.AdNode;
import com.powershop.utils.PageResult;

import java.util.List;

public interface ContentCategoryService {
    List<TbContentCategory> electContentCategoryByParentId(Long id);

    void insertContentCategory(TbContentCategory tbContentCategory);

    Boolean deleteContentCategoryById(Long categoryId);

    void updateContentCategory(TbContentCategory tbContentCategory);

    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    void insertTbContent(TbContent tbContent);

    void deleteContentByIds(Long[] ids);

    List<AdNode> selectFrontendContentByAD();
}
