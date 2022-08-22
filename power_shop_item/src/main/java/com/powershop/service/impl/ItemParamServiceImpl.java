package com.powershop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powershop.mapper.TbItemParamMapper;
import com.powershop.pojo.TbItem;
import com.powershop.pojo.TbItemExample;
import com.powershop.pojo.TbItemParam;
import com.powershop.pojo.TbItemParamExample;
import com.powershop.service.ItemParamService;
import com.powershop.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample itemParamExample=new TbItemParamExample();
        TbItemParamExample.Criteria criteria = itemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        return tbItemParamMapper.selectByExampleWithBLOBs(itemParamExample).get(0);
    }

    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        //1.分页
        PageHelper.startPage(page, rows);

        //2.查询商品信息
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        tbItemParamExample .setOrderByClause("updated DESC");
        List<TbItemParam> tbItemParamList =this.tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        //3.返回PageResult
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(tbItemParamList);
        return new PageResult(pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public void insertItemParam(Long itemCatId, String paramData) {
        TbItemParamExample tbItemParamExample=new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        //1、判断该类别的商品是否有规格模板
        List<TbItemParam> itemParamList = tbItemParamMapper.selectByExample(tbItemParamExample);
        if(itemParamList.size()>0){
            return;
        }
        //2、保存规格模板
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setUpdated(new Date());
        tbItemParam.setCreated(new Date());
        tbItemParamMapper.insertSelective(tbItemParam);
        return ;
    }

    @Override
    public void deleteItemParamById(Long id) {
        tbItemParamMapper.deleteByPrimaryKey(id);
    }
}
