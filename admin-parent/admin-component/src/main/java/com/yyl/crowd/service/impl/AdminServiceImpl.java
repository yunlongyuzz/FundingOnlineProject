package com.yyl.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.entity.AdminExample;
import com.yyl.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.yyl.crowd.exception.LoginFailedException;
import com.yyl.crowd.mapper.AdminMapper;
import com.yyl.crowd.service.api.AdminService;
import com.yyl.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void SaveAdmin(Admin admin) {

        // 1.密码加密
        String source = admin.getUserPswd();
//        userPswd = CrowdUtil.md5(userPswd);
        String userPswd = passwordEncoder.encode(source);
        admin.setUserPswd(userPswd);

        //2.生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        //3.执行保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
            }
        }
    }



    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.根据登录账号查询Admin对象
        AdminExample adminExample = new AdminExample();

        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(loginAcct);

        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2.判断Admin对象是否为null
        if (list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = list.get(0);

        // 3.如果Admin对象为null则抛出异常
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswDB = admin.getUserPswd();

        // 5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        // 6.对面进行比较
        if (!Objects.equals(userPswDB,userPswdForm)){
            // 7.如果比较结果是不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1.调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象
        PageInfo<Admin> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public void remove(Integer adminID) {
        adminMapper.deleteByPrimaryKey(adminID);

    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {

        //表示有选择的更新，有null的字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {

            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {

        // 1.根据 adminId 删除旧的关联关系数据
        adminMapper.deleteOLdRelationship(adminId);

        if(roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }

    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> list = adminMapper.selectByExample(example);
        Admin admin = list.get(0);
        return admin;
    }

}
