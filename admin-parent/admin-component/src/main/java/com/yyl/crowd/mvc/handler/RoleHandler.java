package com.yyl.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.yyl.crowd.entity.Role;
import com.yyl.crowd.service.api.RoleService;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色分页功能
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    @PreAuthorize("hasAuthority('role:get')")
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "")String keyword
    ){

       //调用Service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

            return ResultEntity.successWithData(pageInfo);
    }


    /**
     * 角色保存功能
     * @param roleName
     * @return
     */
    @RequestMapping("/role/save.json")
    public ResultEntity<String> savaRole(
            @RequestParam("name")String roleName){

        roleService.saveRole(new Role(null,roleName));

        return ResultEntity.successWithoutData();
    }

    /**
     * 角色更新
     * @param role
     * @return
     */
    @RequestMapping("role/update.json")
    public ResultEntity<String> updateRole(Role role) {
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 批量移除功能
     * @param roleIdList
     * @return
     */
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdAarry(
            @RequestBody List<Integer> roleIdList) {

        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
        
    }


}
