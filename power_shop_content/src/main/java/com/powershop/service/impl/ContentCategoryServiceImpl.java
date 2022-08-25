package com.powershop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powershop.mapper.TbContentCategoryMapper;
import com.powershop.mapper.TbContentMapper;
import com.powershop.pojo.*;
import com.powershop.service.ContentCategoryService;
import com.powershop.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;

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
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
    }

    @Override
    public void deleteContentByIds(Long[] ids) {
        for (Long id : ids) {
            tbContentMapper.deleteByPrimaryKey(id);
        }
    }
}
