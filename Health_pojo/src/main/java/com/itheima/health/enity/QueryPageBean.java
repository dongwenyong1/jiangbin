package com.itheima.health.enity;

import java.io.Serializable;

/**
 * @author ${dong}
 * @date 2019/12/31 23:28
 *//*用于封装查询条件*/
public class QueryPageBean implements Serializable {
 private Integer currentPage;   //当前页
 private  Integer pageSize;   //每页条数
 private String queryString;   //查询条件

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

}
