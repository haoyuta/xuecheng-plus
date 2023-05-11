package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @className: TeachplanMapperTests
 * @description: TODO 类描述
 * @author: Mr.JiangXinYu
 * @date: 2023/05/11 8:27
 * @Company: Copyright© [日期] by [作者或个人]
 **/
@SpringBootTest
public class TeachplanMapperTests {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Test
    public void teachPlanTest(){

        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(22L);

        System.out.println(teachplanDtos);

    }
}
