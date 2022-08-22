package com.powershop.utils;

import java.io.Serializable;
import java.util.List;

/**
* 分页模型
*/
public class PageResult implements Serializable {
    private Integer pageIndex; //当前页
    private Integer totalPage; //总页数
    private List result; //结果集

    public PageResult() {
    }

    public PageResult(Integer pageIndex, Integer totalPage, List result) {
        this.pageIndex = pageIndex;
        this.totalPage = totalPage;
        this.result = result;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }
}