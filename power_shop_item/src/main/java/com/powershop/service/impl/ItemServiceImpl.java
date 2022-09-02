package com.powershop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powershop.mapper.TbItemCatMapper;
import com.powershop.mapper.TbItemDescMapper;
import com.powershop.mapper.TbItemMapper;
import com.powershop.mapper.TbItemParamItemMapper;
import com.powershop.pojo.*;
import com.powershop.service.ItemService;
import com.powershop.utils.IDUtils;
import com.powershop.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;


    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        //1.分页
        PageHelper.startPage(page, rows);

        //2.查询商品信息
        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);

        //3.返回PageResult
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItemList);
        return new PageResult(pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public void insertTbItem(TbItem tbItem, String desc, String itemParams) {
        /**保存商品信息**/
        //id
        long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItemMapper.insert(tbItem);

        /**保存商品描述**/
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.insert(tbItemDesc);

        /**保存商品规格参数**/
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(new Date());
        tbItemParamItem.setUpdated(new Date());
        tbItemParamItemMapper.insert(tbItemParamItem);
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        //查商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //查类目名称
        String itemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid()).getName();
        //查描述
        String itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId).getItemDesc();
        //查规格参数
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = tbItemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        String itemParamItem = tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample).get(0).getParamData();
        Map<String, Object> map = new HashMap<>();
        map.put("item", tbItem);
        map.put("itemCat", itemCat);
        map.put("itemDesc", itemDesc);
        map.put("itemParamItem", itemParamItem);
        return map;
    }

    @Override
    public void updateTbItem(TbItem tbItem, String desc, String itemParams) {
        /**修改商品信息**/
        tbItem.setUpdated(new Date());
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
        /**修改商品描述**/
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);
        /**修改商品规格参数**/
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(new Date());
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = tbItemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(tbItem.getId());
        tbItemParamItemMapper.updateByExampleSelective(tbItemParamItem, tbItemParamItemExample);
    }

    @Override
    public void deleteItemById(Long itemId) {
      tbItemMapper.deleteByPrimaryKey(itemId);
    }

    @Override
    public List<SearchItem> selectSearchItem(int page, int rows) {
        PageHelper.startPage(page,rows);
        return tbItemMapper.selectSearchItem();
    }
}
