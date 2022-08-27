package com.powershop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powershop.mapper.TbContentCategoryMapper;
import com.powershop.mapper.TbContentMapper;
import com.powershop.pojo.*;
import com.powershop.service.ContentCategoryService;
import com.powershop.utils.AdNode;
import com.powershop.utils.CatResult;
import com.powershop.utils.PageResult;
import com.powershop.utils.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;
    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;
    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;
    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;
    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Value("${PORTAL_AD_KEY}")
    private String PORTAL_AD_KEY;
    @Autowired
    private RedisClient redisClient;

    @Override
    public List<TbContentCategory> electContentCategoryByParentId(Long id) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        return tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
    }

    @Override
    public void insertContentCategory(TbContentCategory tbContentCategory) {
        //保存信息
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.insert(tbContentCategory);
        //如果不是他爹，变成他爹
        TbContentCategory tbContentCategoryParent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        if (!tbContentCategoryParent.getIsParent()) {
            tbContentCategory.setIsParent(true);
            tbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKey(tbContentCategoryParent);
        }
    }

    @Override
    public Boolean deleteContentCategoryById(Long categoryId) {
        //如果有子节点则不能删除
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(categoryId);
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        if (tbContentCategoryExample != null && tbContentCategoryList.size() > 0) {
            return false;
        }
        //在删除之前先查询他爹的ID，不然删除了就没办法查询了
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        //删除内容分类
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //如果是他爹不是爹，改成不是他爹
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        //查询他的兄弟
        List<TbContentCategory> tbContentCategoryBList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //判断如果他的没有兄弟，则把他爹改成不是他爹
        if (tbContentCategoryBList == null && tbContentCategoryBList.size() == 0) {
            TbContentCategory tbContentCategoryParent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getId());
            tbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKey(tbContentCategoryParent);
        }
        return true;
    }

    @Override
    public void updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId) {
        //1.分页
        PageHelper.startPage(page, rows);

        //2.查询商品信息
        TbContentExample tbContentExample=new TbContentExample();
        tbContentExample.setOrderByClause("updated DESC");
        List<TbContent> tbContentList = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);

        //3.返回PageResult
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContentList);
        return new PageResult(pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getList());

    }

    @Override
    public void insertTbContent(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        tbContent.setCreated(new Date());
        this.tbContentMapper.insertSelective(tbContent);
        //缓存同步
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
    }

    @Override
    public void deleteContentByIds(Long[] ids) {
        for (Long id : ids) {
            tbContentMapper.deleteByPrimaryKey(id);
        }
        //缓存同步
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
    }

    @Override
    public List<AdNode> selectFrontendContentByAD() {
        //查询缓存
        List<AdNode> adNodeListRedis = (List<AdNode>)redisClient.hget(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        if(adNodeListRedis!=null&&adNodeListRedis.size()>0){
            return adNodeListRedis;
        }
        // 查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> tbContentList =
                tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> adNodeList = new ArrayList<AdNode>();
        for (TbContent tbContent : tbContentList) {
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(AD_HEIGHT);
            adNode.setWidth(AD_WIDTH);
            adNode.setHeightB(AD_HEIGHTB);
            adNode.setWidthB(AD_WIDTHB);
            adNodeList.add(adNode);
        }
        //添加到缓存
        redisClient.hset(PORTAL_AD_KEY,AD_CATEGORY_ID.toString(),adNodeList);
        return adNodeList;
    }
}
