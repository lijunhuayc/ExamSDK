package com.ljh.custom.base_library.model;

/**
 * @author shun
 * @version V1.0
 * @Package com.cos.gdt.ui.widget.title
 * @date 16/3/10 10:11
 */
public class PageBean {
    private int pageNumber;//": 0, 当前页码
    private int pageSize;//": 0," 分页数据量
    private int pageCount;//": 0,   页总数
    private int recordCount;//": 0    数据总数

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPagesize() {
        return pageSize;
    }

    public void setPagesize(int pagesize) {
        this.pageSize = pagesize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

}
