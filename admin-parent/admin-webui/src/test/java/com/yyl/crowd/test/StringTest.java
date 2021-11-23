package com.yyl.crowd.test;

import com.yyl.crowd.entity.Admin;
import com.yyl.crowd.mapper.AdminMapper;
import com.yyl.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StringTest {

    @Test
    public void testMd5() {
        String source = "123123";
        String encoded = CrowdUtil.md5(source);
        System.out.println(encoded);

    }

    @Test
    public void testRawPassword(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String source = "123123";

        String encode = passwordEncoder.encode(source);

        System.out.println(encode);

    }



}