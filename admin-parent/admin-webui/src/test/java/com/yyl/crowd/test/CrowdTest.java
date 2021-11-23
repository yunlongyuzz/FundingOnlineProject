package com.yyl.crowd.test;

import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.entity.Role;
import com.yyl.crowd.mapper.AdminMapper;
import com.yyl.crowd.mapper.RoleMapper;
import com.yyl.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//spring整合junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testRoleSava(){
        for (int i =0;i<100;i++){
            roleMapper.insert(new Role(null,"role"+i));
        }
    }


    @Test
    public void testSaveAdminMulti() {
        for (int i = 0; i < 200; i++) {
            adminMapper.insert(new Admin(null, "loginAcct" + i, "userPswd" + i, "userName" + i, "email" + i + "@qq.com", null));
        }
    }

    @Test
    public void testSaveAdmin(){
        Admin admin = new Admin(null, "jerry", "12345", "杰瑞", "jerry@qq.com", null);
        adminService.SaveAdmin(admin);
    }

    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null, "tom", "1234", "汤姆", "tom@qq.com", null);
        adminMapper.insert(admin);
    }


    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);


    }



}
