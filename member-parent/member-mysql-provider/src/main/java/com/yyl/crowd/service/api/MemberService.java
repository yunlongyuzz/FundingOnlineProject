package com.yyl.crowd.service.api;

import com.yyl.crowd.entity.po.MemberPO;

public interface MemberService {

    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
