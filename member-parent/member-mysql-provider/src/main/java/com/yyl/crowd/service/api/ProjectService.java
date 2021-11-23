package com.yyl.crowd.service.api;

import com.yyl.crowd.entity.po.ProjectPO;
import com.yyl.crowd.entity.vo.DetailProjectVO;
import com.yyl.crowd.entity.vo.PortalProjectVO;
import com.yyl.crowd.entity.vo.PortalTypeVO;
import com.yyl.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);

    List<PortalProjectVO> getAllProject();

}
