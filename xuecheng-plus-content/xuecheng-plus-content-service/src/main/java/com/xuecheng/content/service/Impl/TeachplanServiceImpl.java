package com.xuecheng.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @className: TeachplanServiceImpl
 * @description: TODO 类描述
 * @author: Mr.JiangXinYu
 * @date: 2023/05/11 8:37
 * @Company: Copyright© [日期] by [作者或个人]
 **/
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;


    @Override
    public List<TeachplanDto> selectTreeNodes(Long courseId) {

        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id!=null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }else{
            //取出同父同级别的课程计划数量
            int count=getTeachplanCount(teachplanDto.getCourseId(),teachplanDto.getParentid());

            //设置排序号
            Teachplan teachplanNew=new Teachplan();
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);
        }
    }


    /**
     * @description 获取最新的排序号，计算父节点下有多少个子节点
     * @param courseId  课程id
     * @param parentId  父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanCount(long courseId,long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper =new LambdaQueryWrapper<>();

        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);

        return count;
    }

    /**
     * 课程计划删除
     * @param id
     */
    @Override
    public void deleteTeachplan(Long id) {
        //删除大章节，大章节下有小章节时不允许删除。
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan.getParentid()==0){
            //没有子节点，可以删除
            if(getTeachplanCount(teachplan.getCourseId(),teachplan.getId())==0){
                    teachplanMapper.deleteById(teachplan.getId());
            }else{
                //存在子节点，抛异常
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }
        }else{
            //删除小章节，同时将关联的信息进行删除。
            teachplanMapper.deleteById(teachplan.getId());
        }
    }

    /**
     * 课程计划上移
     * @param id
     */
    @Override
    public void moveUp(Long id) {
     //获取对象
        Teachplan teachplan = teachplanMapper.selectById(id);
        int orderBy=teachplan.getOrderby();
        if(orderBy==1){
            throw new XueChengPlusException("已是最上层，无法再上移");
        }else {
            //向上移动之前，要保证原本上一层的数据Orderby属性加1
            //1.获取上一层数据对象
            LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper();
            queryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
            queryWrapper.eq(Teachplan::getGrade,teachplan.getGrade());
            int orderBy2=orderBy-1;
            queryWrapper.eq(Teachplan::getOrderby,orderBy2);
            queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
            Teachplan teachplanBefore = teachplanMapper.selectOne(queryWrapper);

            //2.将上一层数据对象的Orderby属性加1
            teachplanBefore.setOrderby(teachplanBefore.getOrderby()+1);

            //3.更新上一层数据对象
            teachplanMapper.updateById(teachplanBefore);

            //向上移一层
            teachplan.setOrderby(orderBy2);
            teachplanMapper.updateById(teachplan);

        }
    }

    /**
     * 课程计划下移
     * @param id
     */
    @Override
    public void moveDown(Long id) {
        //获取对象
        Teachplan teachplan = teachplanMapper.selectById(id);
        //获取当前排序位置
        int orderBy=teachplan.getOrderby();

        //先判断下一层对象是否存在，存在下移时下一层数据对象的Orderby属性减1
        //1.获取下一层数据对象
        LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
        queryWrapper.eq(Teachplan::getGrade,teachplan.getGrade());
        int orderBy2=orderBy+1;
        queryWrapper.eq(Teachplan::getOrderby,orderBy2);
        queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
        Teachplan teachplanAfter = teachplanMapper.selectOne(queryWrapper);

        //2.判断是否存在
        if(teachplanAfter==null){
            throw new XueChengPlusException("已是最下层，无法再下移");
        }else{
            teachplanAfter.setOrderby(orderBy);
            teachplanMapper.updateById(teachplanAfter);
        }

        //向下移1层
        teachplan.setOrderby(orderBy2);
        teachplanMapper.updateById(teachplan);
    }
}
