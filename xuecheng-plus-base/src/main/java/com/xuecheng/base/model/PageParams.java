package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.java.Log;

/**
 * @description 分页查询通用参数
 * @author Mr.M
 * @date 2023/4/26 14:02
 * @version 1.0
 */
@Data
@ToString
public class PageParams {

    //当前页码
    //swagger提供的注解
    @ApiModelProperty(value = "分页页码")
    private Long pageNo = 1L;

    //每页记录数默认值
    @ApiModelProperty(value = "每页记录数")
    private Long pageSize =10L;

    public PageParams(){

    }

    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}