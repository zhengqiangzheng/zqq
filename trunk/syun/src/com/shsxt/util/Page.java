package com.shsxt.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zq on 2018/8/22/022 9:36
 * @author zq
 */
@Getter@Setter
public class Page<T> {
    /**当前页**/
    private  Integer pageNum;
   /**一页要显示多少个记录**/
    private  Integer pageSize;
    /**  共有多少云记数量,可以从数据库中查到 */
    private Integer Total;

  /*一共有多少页Total/pagenum 往上取整*/
    private Integer totalPages;
    //上一页
    private Integer prePage;
    //下一页
    private Integer nextPage;
    //导航栏开始页
    private Integer startNavpage;
    // 导航栏结束页
    private Integer endNavpage;
    //数据列表,数据库查
    private List<T>datas;



    public Page(Integer pageNum, Integer pageSize, Integer Total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.Total = Total;
        //总页数
        totalPages = (int) Math.ceil(Total*1.0/ pageSize);
        //上一页
        prePage=pageNum-1>0?pageNum-1:1;
        //下一页
        nextPage=pageNum+1>totalPages?totalPages:pageNum+1;
        //导航栏开始页
        startNavpage=pageNum-5;
        if (startNavpage<1) {
            startNavpage=1;
            endNavpage=startNavpage+9>totalPages?totalPages:startNavpage+9;
        }
        if (endNavpage>totalPages) {
            endNavpage=totalPages;
            startNavpage=endNavpage-9<0?1:endNavpage-9;

        }


        //导航栏结束页


    }

}
