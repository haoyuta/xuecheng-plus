package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 *  @description 课程基本信息管理业务接口
 */
public interface CourseBaseInfoService {

    /*
     * @description 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 条件条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     * @author Mr.M
     * @date 2022/9/6 21:44
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * @description 课程新增接口
     * @param companyId
     * @param addCourseDto
     * @return
     */
    CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);


    /**
     * 根据课程id回显数据
     * @param courseId
     * @return
     */
    CourseBaseInfoDto getCourseBaseById(Long courseId);


    /**
     * 修改课程
     * @param companyId  机构id
     * @param editCourseDto  修改课程信息
     * @return  课程详细信息
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);




}
