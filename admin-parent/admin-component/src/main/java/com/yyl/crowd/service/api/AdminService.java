package com.yyl.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.yyl.crowd.entity.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminService {

   void SaveAdmin(Admin admin);

   List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);

    void remove(Integer adminID);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
