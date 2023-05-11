package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @description 课程分类树型结点dto
 * @author Mr.M
 * @date 2022/9/7 15:16
 * @version 1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    //根据前端返回的json数据格式，在原有CourseCategory基础上添加集合属性childrenTreeNodes，用于接收json数据
    List<CourseCategoryTreeDto> childrenTreeNodes;
}