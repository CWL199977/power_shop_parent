package com.powershop.controller;

import com.powershop.pojo.TbContent;
import com.powershop.pojo.TbContentCategory;
import com.powershop.pojo.TbItem;
import com.powershop.service.ContentCategoryService;
import com.powershop.utils.PageResult;
import com.powershop.utils.Result;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 内容分类管理查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0") Long id) {
        try {
            List<TbContentCategory> tbContentCategoryList = contentCategoryService.electContentCategoryByParentId(id);
            return Result.ok(tbContentCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 内容分类管理添加
     *
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory) {
        try {
            contentCategoryService.insertContentCategory(tbContentCategory);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    /**
     * 内容分类管理删除
     *
     * @param categoryId
     * @return
     */
    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(Long categoryId) {
        try {
            Boolean result = contentCategoryService.deleteContentCategoryById(categoryId);
            if (!result) {
                return Result.error("父节点不允许删除");
            }
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }

    /**
     * 内容分类管理修改
     *
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(TbContentCategory tbContentCategory) {
        try {
            contentCategoryService.updateContentCategory(tbContentCategory);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败");
        }
    }


    /**
     * 分页查询商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer rows,
                                        Long categoryId) {
        try {
            PageResult pageResult = contentCategoryService.selectTbContentAllByCategoryId(page, rows,categoryId);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
    /**
     * 商品的添加
     *
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent) {
        try {
            contentCategoryService.insertTbContent(tbContent);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }
    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(Long[] ids){
        try {
                contentCategoryService.deleteContentByIds(ids);
                return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }

    }

}
