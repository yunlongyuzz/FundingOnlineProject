package com.yyl.crowd.mvc.interceptor;

import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1.通过request对象获取Session对象
        HttpSession session = request.getSession();

        // 2.尝试从Session域中获取Admin对象
         Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

         // 3.判断admin对象是否为空
        if (admin == null){
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        }

        return true;
    }

}
