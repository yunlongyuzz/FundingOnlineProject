package com.yyl.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/do/crowd/launch/page").setViewName("project-launch");
        registry.addViewController("/agree/protocol/page").setViewName("project-agree");
        registry.addViewController("/return/info/page").setViewName("project-return");
        registry.addViewController("/create/success").setViewName("project-success");


    }


}
