package com.yyl.crowd.mvc.config;

import com.google.gson.Gson;
import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.exception.AccessForbiddenException;
import com.yyl.crowd.exception.LoginAcctAlreadyInUseException;
import com.yyl.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.yyl.crowd.exception.LoginFailedException;
import com.yyl.crowd.util.CrowdUtil;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 表示当前类是一个基于注解的异常处理器类
 * 异常出发后要去的地方
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    //更新时账号被使用
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String modelAndView = "admin-edit";

        //返回modelAndView对象
        return commonResolve(modelAndView,exception,request,response);

    }

    //账号被使用
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String modelAndView = "admin-add";

        //返回modelAndView对象
        return commonResolve(modelAndView,exception,request,response);

    }

    //登陆失败
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String modelAndView = "admin-login";

        //返回modelAndView对象
        return commonResolve(modelAndView,exception,request,response);

    }

    //访问被拒绝
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception, HttpServletRequest request, HttpServletResponse response ) throws IOException {

        String modelAndView = "system-error";

        return commonResolve(modelAndView, exception, request, response);
    }


    private ModelAndView commonResolve(String viewName,Exception exception,HttpServletRequest request,HttpServletResponse response) throws IOException {


        //1.判断当前类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        //2.如果是Ajax请求
        if (judgeResult){

            //3.创建ResultEntity
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            //4.创建Gson对象
            Gson gson = new Gson();

            //5.将ResultEntity对象转换成JSON字符串
            String json = gson.toJson(resultEntity);

            //6.将JSON字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            //7.由于上面已经通过原生的response对象返回了响应，所以不再提供ModelAndView对象
            return null;

        }

        //8.如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //9.将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);

        //10.设置对应的视图
        modelAndView.setViewName(viewName);

        //11.返回modelAndView对象
        return modelAndView;

    }


}
