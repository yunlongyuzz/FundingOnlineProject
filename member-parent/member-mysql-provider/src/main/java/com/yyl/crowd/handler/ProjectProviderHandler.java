package com.yyl.crowd.handler;


import com.yyl.crowd.entity.po.ProjectPO;
import com.yyl.crowd.entity.vo.DetailProjectVO;
import com.yyl.crowd.entity.vo.PortalProjectVO;
import com.yyl.crowd.entity.vo.PortalTypeVO;
import com.yyl.crowd.entity.vo.ProjectVO;
import com.yyl.crowd.service.api.ProjectService;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;


    /**
     * 点击更多，查看项目信息
     * @return
     */
    @RequestMapping("/get/all/project/data/remote")
    public ResultEntity<List<PortalProjectVO>> getAllProjectRemote(){

        try {

            List<PortalProjectVO> allProject = projectService.getAllProject();

            return ResultEntity.successWithData(allProject);

        }catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }


    /**
     * 查看项目详细信息
     * @param projectId
     * @return
     */
    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(
            @PathVariable("projectId") Integer projectId) {

        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) { e.printStackTrace();
        return ResultEntity.failed(e.getMessage());
        }
    }



    /**
     * 首页显示项目信息
     * @return
     */
    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote() {
        try {

            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVO();

            return ResultEntity.successWithData(portalTypeVOList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }



    /**
     *保存项目
     * @param projectVO
     * @param memberId
     * @return
     */
    @RequestMapping("/save/project/vo/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                                    @RequestParam("memberId") Integer memberId) {

        try {
            // 调用“本地”Service 执行保存
            projectService.saveProject(projectVO, memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
