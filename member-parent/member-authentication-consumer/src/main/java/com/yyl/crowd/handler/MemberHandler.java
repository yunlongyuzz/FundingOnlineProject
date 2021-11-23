package com.yyl.crowd.handler;

import com.yyl.crowd.api.MySQLRemoteService;
import com.yyl.crowd.api.RedisRemoteService;
import com.yyl.crowd.config.ShortMessageProperties;
import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.entity.po.MemberPO;
import com.yyl.crowd.entity.vo.MemberLoginVO;
import com.yyl.crowd.entity.vo.MemberVO;
import com.yyl.crowd.util.CrowdUtil;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:http://localhost";
    }


    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap,
                        HttpSession httpSession) {
        // 1.调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 2.比较密码
        String userpswdDataBase = memberPO.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matchesResult = bCryptPasswordEncoder.matches(userpswd, userpswdDataBase);
        if (!matchesResult) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 3.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:http://localhost/auth/member/to/center/page";
    }


    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 2.从redis读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(phoneNum);
        // 3.查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        // 4.如果能够到value则比较表单的验证码和redis的验证码
        String formCode = memberVO.getCode();
        if (!Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        // 5.如果验证码一致，则从redis删除
        redisRemoteService.removeRedisKeyRemote(phoneNum);

        // 6.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encode = passwordEncoder.encode(userpswd);
        memberVO.setUserpswd(encode);

        // 7.执行保存
        // 创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();
        // 复制属性
        BeanUtils.copyProperties(memberVO, memberPO);
        // 调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);
        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:http://localhost/auth/member/to/login/page";
    }



    @ResponseBody
    @RequestMapping(value = "/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(
            @RequestParam(value = "phone_number") String phone_number) {

        // 1.发送验证码到手机
        ResultEntity<String> sendMeassageResultEntity = CrowdUtil.sendCodeByShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(),
                shortMessageProperties.getAppCode(),
                phone_number,
                shortMessageProperties.getContent(),
                shortMessageProperties.getTemplate_id());
        // 2,判断短信发送的结果
        if (ResultEntity.SUCCESS.equals(sendMeassageResultEntity.getResult())) {
            // 3.如果发送成功，则将验证码存入redis
            String code = sendMeassageResultEntity.getData();

            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(phone_number, code, 5, TimeUnit.MINUTES);


            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMeassageResultEntity;
        }
    }




}
