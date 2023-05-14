package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @className: CourseTeacherController
 * @description: 课程教师信息管理接口
 * @date: 2023/05/12 17:06
 **/
@Slf4j
@RestController
@Api(value = "课程教师信息管理接口",tags = "课程教师信息管理接口")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("课程教师信息查询")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getByCourseId(@PathVariable Long courseId){

        /**
         * 返回课程所有的任课老师
         */
        return courseTeacherService.getByCourseId(courseId);

    }

    @ApiOperation("课程教师添加")
    @PostMapping("/courseTeacher")
    public CourseTeacher addCourseTeacher(@RequestBody CourseTeacher courseTeacher){

        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;

        return courseTeacherService.addCourseTeacher(companyId,courseTeacher);

    }

    @ApiOperation("修改课程教师信息")
    @PutMapping("/courseTeacher")
    public CourseTeacher updateCourseTeacher(@RequestBody CourseTeacher courseTeacher){

        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;

        return courseTeacherService.updateCourseTeacher(companyId,courseTeacher);
    }


    @ApiOperation("删除课程教师信息")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteCourseTeacher(@PathVariable Long courseId,@PathVariable Long id){

        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;

        courseTeacherService.deleteCourseTeacher(companyId,courseId,id);
    }
}
