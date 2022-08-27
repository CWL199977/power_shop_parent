package com.powershop.service.impl;

import com.powershop.mapper.TbItemCatMapper;
import com.powershop.pojo.TbItemCat;
import com.powershop.pojo.TbItemCatExample;
import com.powershop.service.ItemCatService;
import com.powershop.utils.CatNode;
import com.powershop.utils.CatResult;
import com.powershop.utils.RedisClient;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private RedisClient redisClient;
    @Value("${PROTAL_CATRESULT_KEY}")
    private String PROTAL_CATRESULT_KEY;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample tbItemCatExample=new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        return tbItemCatMapper.selectByExample(tbItemCatExample);

    }

    @Override
    public CatResult selectItemCategoryAll() {
        //查询缓存
        CatResult catResultRedis =(CatResult) redisClient.get(PROTAL_CATRESULT_KEY);
        if(catResultRedis!=null){
            return catResultRedis;
        }
        CatResult catResult = new CatResult();
        //查询商品分类
        catResult.setData(getCatList(0L));

        //添加到缓存
        redisClient.set(PROTAL_CATRESULT_KEY,catResult);

        return catResult;
    }
    private List<?> getCatList(Long parentId){
        //创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = this.tbItemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int count = 0;
        for(TbItemCat tbItemCat:list){
            //判断是否是父节点
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));//查询子节点
                resultList.add(catNode);
            }else{
                resultList.add(tbItemCat.getName());
            }
        }
        return resultList;
    }
}
