package com.yyl.crowd.mvc.handler;

import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.entity.Student;
import com.yyl.crowd.service.api.AdminService;
import com.yyl.crowd.util.CrowdUtil;
import com.yyl.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        logger.info("judgeResult="+judgeResult);

        logger.info(student.toString());

        return ResultEntity.successWithData(student);


    }



    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        logger.info("judgeResult="+judgeResult);

        List<Admin> adminList = adminService.getAll();

        modelMap.addAttribute("adminList",adminList);

        System.out.println(10/0);


        return "target";

    }


}
