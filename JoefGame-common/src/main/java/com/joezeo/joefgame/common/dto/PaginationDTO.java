package com.joezeo.joefgame.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> implements Serializable {

    private static final long serialVersionUID = 3659016553387318138L;


    private List<T> datas; // 根据分页信息获取到的相应页数的数据
    private Boolean hasNext; // 判断是否展示上一页按钮
    private Boolean hasPrevious; // 判断是否展示下一页按钮
    private Boolean hasFirst; // 判断是否展示转到第一页按钮
    private Boolean hasLast; // 判断是都展示转到最后一页按钮
    private Integer page; // 当前页数
    private Integer size; // 每页展示数据数量
    private Integer totalCount; // 总数据条数
    private Integer totalPage; // 总页数
    private List<Integer> pages = new ArrayList<>(); // 分页条上一共要展示的页数集合

    private String condition; // 搜索时需要的搜索条件

    public void setPagination(Integer page, Integer size, Integer totalCount) {
        this.totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage && totalPage!=0){
            page = totalPage;
        }

        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        pages.add(page);
        for(int i=1; i<=3; i++){
            if(page-i > 0){
                pages.add(0, page-i);
            }
            if(page+i <= totalPage){
                pages.add(page+i);
            }
        }

        hasNext = page != totalPage && totalPage!=0;
        hasPrevious = page != 1;
        hasFirst = (page>=5);
        hasLast = (page<=totalPage-4);
    }
}
