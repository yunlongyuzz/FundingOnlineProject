package com.yyl.crowd.mvc.handler;

import com.yyl.crowd.entity.Auth;
import com.yyl.crowd.entity.Role;
import com.yyl.crowd.service.api.AdminService;
import com.yyl.crowd.service.api.AuthService;
import com.yyl.crowd.service.api.RoleService;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;


    /**
     * 分配角色权限
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelathinship(
            @RequestBody Map<String, List<Integer>> map) {
        authService.saveRoleAuthRelathinship(map);
        return ResultEntity.successWithoutData();
    }


    /**
     * 看到每个角色所拥有的权限
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId") Integer roleId) {

        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);

        return ResultEntity.successWithData(authIdList);
    }


    /**
     * 看到所有角色权限
     * @return
     */
    @ResponseBody
    @RequestMapping("/assgin/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){

        List<Auth> authList = authService.getAll();

        return ResultEntity.successWithData(authList);
    }


    //* 用户维护是用页面转发的方式做的
    /*/
       * 保存用户和角色的关系
     */
    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            @RequestParam(value="roleIdList",required = false) List<Integer> roleIdList ) {

        adminService.saveAdminRoleRelationship(adminId, roleIdList);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    /**
     * 获取已分配和未分配的角色
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap ) {
        // 1.查询已分配角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 2.查询未分配角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
        // 3.存入模型（本质上其实是：request.setAttribute("attrName",attrValue);
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);
        return "assign-role";
    }



}
