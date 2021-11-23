package com.yyl.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    /**
     * 用户更新
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("keyword")String keyword){

        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;

    }

    /**
     * 切换页数，去其它页
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId")Integer adminId,
            ModelMap modelMap
    ){
        Admin admin = adminService.getAdminById(adminId);

        modelMap.addAttribute("admin",admin);

        return "admin-edit";

    }

    /**
     * 用户保存
     * @param admin
     * @return
     */
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/sava.html")
    public String sava(Admin admin){

        adminService.SaveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /**
     * 移除用户
     * @param adminID
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId")Integer adminID,
            @PathVariable("pageNum")Integer pageNum,
            @PathVariable("keyword")String keyword
                    ){

        adminService.remove(adminID);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;

    }

    /**
     * 获取用户分页信息
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/get/page.html")
    public String getAdminPage(
            //defaultValue属性为空字符串表示浏览器不提供关键
            @RequestParam(value = "keyword",defaultValue = "")String keyword,
            //浏览器未提供pageNum时，默认前往第一页
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            //浏览器未提供pageSize时，默认每页显示5条记录
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            ModelMap modelMap
            ){

        //查询得到分页数据
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);


        //将分页数据存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin-page";
    }


    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("admin/do/logout.html")
    public String doLogout(HttpSession session){

        //强制Session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";

    }

    /**
     * 登录功能
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct")String loginAcct,
            @RequestParam("userPswd")String userPswd,
            HttpSession session
            ){

        // 这个方法如果能够返回Admin，说明登录成功，如果账号密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);

        // 将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        return "redirect:/admin/to/main/page.html";

    }

}
