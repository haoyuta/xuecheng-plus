package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description 课程基本信息管理业务接口
 */
public interface TeachplanService {

    /**
     * 查询某课程的课程计划，组成树型结构
     * @param courseId
     * @return
     */
    public List<TeachplanDto> selectTreeNodes(Long courseId);


    /**
     * 课程计划创建或修改
     * @param teachplan
     */
    public void saveTeachplan(SaveTeachplanDto teachplan);


    /**
     * 课程计划删除
     * @param id
     */
    public void deleteTeachplan(Long id);

    /**
     * 课程计划上移
     * @param id
     */
    public void moveUp(Long id);

    /**
     * 课程计划下移
     * @param id
     */
    public void moveDown(Long id);

}
