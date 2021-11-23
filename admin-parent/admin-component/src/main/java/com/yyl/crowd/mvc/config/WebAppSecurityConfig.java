package com.yyl.crowd.mvc.config;


import com.yyl.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity security) throws Exception {

        String[] permitUrls = {"/index.jsp","/bootstrap/**",
                "/crowd/**","/css/**","/fonts/**","/img/**",
                "/jquery/**","/layer/**","/script/**","/ztree/**","/admin/to/login/page.html"};
        security
                .authorizeRequests()        // 表示对请求进行授权
                .antMatchers(permitUrls)    // 传入的ant风格的url
                .permitAll()                // 允许上面的所有请求，不需要认证

                .antMatchers("/admin/get/page.html")                  // 设置要得到admin的分页信息
                .access("hasRole('经理') or hasAuthority('user:get')")   // 必须具有经理的角色和“user:get”权限二者之一

                .anyRequest()               // 其他未设置的全部请求
                .authenticated()            // 表示需要认证

                .and() .exceptionHandling() .accessDeniedHandler(new AccessDeniedHandler() {
                @Override public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);}
        })

                .and()
                .csrf()         // 设置csrf
                .disable()      // 关闭csrf

                .formLogin()                                       // 开启表单登录功能
                .loginPage("/admin/to/login/page.html")            // 指定登陆页面
                .usernameParameter("loginAcct")                    // 设置表单中对应用户名的标签的name属性名
                .passwordParameter("userPswd")                     // 设置表单中对应密码的标签的name属性名
                .loginProcessingUrl("/security/do/login.html")     // 设置登录请求的提交地址
                .defaultSuccessUrl("/admin/to/main/page.html")     // 设置登陆成功后前往的地址
                .and()
                .logout()                                         // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")            // 设置退出登录的url
                .logoutSuccessUrl("/admin/to/login/page.html")    // 设置退出成功后前往的页面
             ;


    }

}
