package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @className: CourseTeacherService
 * @description: 课程教师信息管理
 * @date: 2023/05/12 17:16
 **/
public interface CourseTeacherService {

    public List<CourseTeacher> getByCourseId(Long courseId);

    public CourseTeacher addCourseTeacher(Long companyId,CourseTeacher courseTeacher);

    public CourseTeacher updateCourseTeacher(Long companyId,CourseTeacher courseTeacher);

    public void deleteCourseTeacher(Long companyId,Long courseId, Long id);

}

