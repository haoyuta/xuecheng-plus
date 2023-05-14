package com.xuecheng.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description 课程信息管理业务接口实现类
 * @author Mr.M
 * @date 2022/9/6 21:45
 * @version 1.0
 */
@Service
public class CourseBaseInfoServiceImpl  implements CourseBaseInfoService {
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;


    /**
     * 分页查询课程基本信息
     * @param pageParams
     * @param queryCourseParamsDto
     * @return
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        //构建查询条件对象0
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //构建查询条件，根据课程名称查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        //构建查询条件，根据课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        //构建查询条件，根据课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());


        //分页对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<CourseBase> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());

        return courseBasePageResult;
    }


    /**
     * 保存新增课程信息，包括基本信息和营销信息
     * @param companyId
     * @param dto
     * @return
     */
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        if (StringUtils.isBlank(dto.getName())) {
            throw new XueChengPlusException("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            throw new XueChengPlusException("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            throw new XueChengPlusException("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            throw new XueChengPlusException("适应人群");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            throw new XueChengPlusException("收费规则为空");
        }
        //新增对象
        CourseBase courseBaseNew = new CourseBase();
        //将填写的课程信息赋值给新增对象
        BeanUtils.copyProperties(dto,courseBaseNew);
        //设置审核状态，默认未提交
        courseBaseNew.setAuditStatus("202002");
        //设置发布状态，默认未发布
        courseBaseNew.setStatus("203001");
        //机构id
        courseBaseNew.setCompanyId(companyId);
        //添加时间
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0){
            //throw new RuntimeException("新增课程基本信息失败");
            throw new XueChengPlusException("新增课程基本信息失败");
        }
        //向课程营销表保存课程营销信息
        //课程营销信息
        CourseMarket courseMarketNew = new CourseMarket();
        Long courseId = courseBaseNew.getId();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseId);
        int i = saveCourseMarket(courseMarketNew);
        if(i<=0){
            //throw new RuntimeException("保存课程营销信息失败");
            throw new XueChengPlusException("保存课程营销信息失败");
        }
        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);

    }

    //单独写一个方法保存营销信息，逻辑：存在则更新，不存在则添加
    //保存课程营销信息
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //收费规则
        String charge = courseMarketNew.getCharge();

        if(StringUtils.isBlank(charge)){
            //throw new RuntimeException("收费规则没有选择");
            throw new XueChengPlusException("保存课程营销信息失败");
        }

        //收费规则为收费
        if(charge.equals("201001")) {
            if (courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue() <= 0) {
                throw new XueChengPlusException("课程的价格不能为空并且必须大于0");
            }
        }
        //根据id从课程营销表查询
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarketNew.getId());
        if(courseMarketObj == null){
            return courseMarketMapper.insert(courseMarketNew);
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }


    //根据课程id查询课程基本信息，包括基本信息和营销信息，并将其赋值给courseBaseInfoDto
    public CourseBaseInfoDto getCourseBaseInfo(long courseId){

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        //查询课程基本信息和营销信息，并将其赋值给courseBaseInfoDto
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        //查询分类名称
        //查询大分类
        CourseCategory courseCategory1 = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategory1.getName());
        //查询小分类
        CourseCategory courseCategory2 = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setSt(courseCategory2.getName());

        return courseBaseInfoDto;
    }

    /**
     * 根据课程id回显数据
     * @param courseId
     * @return
     */
    public CourseBaseInfoDto getCourseBaseById(Long courseId){

        //查询课程信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        //查询营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //要返回的对象
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //查询分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;
    }


    /**
     * 修改课程
     * @param companyId  机构id
     * @param editCourseDto  修改课程信息
     * @return  课程详细信息
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //课程id
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase==null){
            XueChengPlusException.cast("课程不存在");
        }

        //校验本机构只能修改本机构的课程
        if(!courseBase.getCompanyId().equals(companyId)){
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }

        //封装基本信息的数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);

        //封装营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        saveCourseMarket(courseMarket);

        //查询课程信息
        CourseBaseInfoDto courseBaseInfo = this.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }

    /**
     * 删除课程,特别注意删除顺序，相关联的要注意
     * @param companyId
     * @param id
     */
    @Override
    @Transactional
    public void deleteCourseBase(Long companyId,Long id) {
        //非本机构无法删除课程
        if(companyId!=1232141425L){
            throw new XueChengPlusException("对不起，您没有权限");
        }

        //根据id查询课程
        CourseBase courseBase=courseBaseMapper.selectById(id);
        //课程的审核状态为未提交时方可删除。
        if(courseBase.getAuditStatus().equals("202002")){

            //1、删除营销信息course_market
            courseMarketMapper.deleteById(id);

            //2、删除课程计划teachplan
            LambdaQueryWrapper<Teachplan> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(Teachplan::getCourseId,id);
            //注意有多个课程计划
            List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper1);
            //遍历集合，根据课程计划id删除课程计划媒资资源
            for (Teachplan teachplan : teachplans) {
                Long teachPlanId = teachplan.getId();

                //删除课程计划媒体资源teachplan_media
                LambdaQueryWrapper<TeachplanMedia> queryWrapper2=new LambdaQueryWrapper<>();
                queryWrapper2.eq(TeachplanMedia::getTeachplanId,teachPlanId);
                teachplanMediaMapper.delete(queryWrapper2);
            }

            //3、删除媒资资源后删除课程计划
            teachplanMapper.delete(queryWrapper1);


            //4、删除课程教师信息course_teacher
            LambdaQueryWrapper<CourseTeacher> queryWrapper3=new LambdaQueryWrapper<>();
            queryWrapper3.eq(CourseTeacher::getCourseId,id);
            courseTeacherMapper.delete(queryWrapper3);

            //5、删除课程相关的基本信息course_base
            courseBaseMapper.deleteById(id);
        }else {
            throw new XueChengPlusException("该课程已提交，无法删除");
        }
    }
}
