package com.xuecheng.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className: CourseTeacherServiceImpl
 * @description: 课程教师信息管理
 * @date: 2023/05/12 17:16
 **/
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);

        return courseTeacherMapper.selectList(queryWrapper);

    }

    @Override
    public CourseTeacher addCourseTeacher(Long companyId, CourseTeacher courseTeacher) {
        //只允许向机构自己的课程中添加老师
        if (companyId != 1232141425L) {
            throw new XueChengPlusException("对不起，您没有权限");
        } else {
            CourseTeacher courseTeacherNew = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher, courseTeacherNew);
            courseTeacherNew.setCreateDate(LocalDateTime.now());

            //添加教师数据
            courseTeacherMapper.insert(courseTeacherNew);

            //返回数据
            return courseTeacherNew;
        }
    }

    @Override
    public CourseTeacher updateCourseTeacher(Long companyId, CourseTeacher courseTeacher) {
        //只允许向机构自己的课程中添加老师
        if (companyId != 1232141425L) {
            throw new XueChengPlusException("对不起，您没有权限");
        } else {
            courseTeacher.setCreateDate(LocalDateTime.now());
            courseTeacherMapper.updateById(courseTeacher);

            return courseTeacher;
        }
    }


    @Override
    public void deleteCourseTeacher(Long companyId, Long courseId, Long id) {
        //只允许向机构自己的课程中添加老师
        if (companyId != 1232141425L) {
            throw new XueChengPlusException("对不起，您没有权限");
        } else {
            LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CourseTeacher::getCourseId, courseId);
            queryWrapper.eq(CourseTeacher::getId, id);

            //删除教师信息
            courseTeacherMapper.delete(queryWrapper);

        }
    }
}
