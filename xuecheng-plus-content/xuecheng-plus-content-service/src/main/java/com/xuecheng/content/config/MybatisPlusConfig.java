package com.xuecheng.content.config;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//扫描mapper包,为mapper创建代理对象
@MapperScan("com.xuecheng.content.mapper")
public class MybatisPlusConfig {

        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor(){
            //1 创建MybatisPlusInterceptor拦截器对象
            MybatisPlusInterceptor mpInterceptor=new MybatisPlusInterceptor();

            //2 添加分页拦截器
            mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
            return mpInterceptor;
        }
    }
