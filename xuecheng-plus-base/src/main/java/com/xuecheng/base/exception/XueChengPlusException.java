package com.xuecheng.base.exception;


import com.xuecheng.base.exception.CommonError;

/**
 * @description 学成在线项目异常类，本项目自定义异常类型
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
public class XueChengPlusException extends RuntimeException {

    //只有一个属性，用于封装错误信息
    private String errMessage;

    //提供两个构造器，有参和无参
    public XueChengPlusException() {
        super();
    }

    public XueChengPlusException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }


    public static void cast(CommonError commonError){
        throw new XueChengPlusException(commonError.getErrMessage());
    }

    public static void cast(String errMessage){
        throw new XueChengPlusException(errMessage);
    }

}