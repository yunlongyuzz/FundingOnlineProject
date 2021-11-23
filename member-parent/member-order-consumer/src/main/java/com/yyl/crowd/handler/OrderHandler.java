package com.yyl.crowd.handler;


import com.yyl.crowd.api.MySQLRemoteService;
import com.yyl.crowd.constant.CrowdConstant;
import com.yyl.crowd.entity.vo.AddressVO;
import com.yyl.crowd.entity.vo.MemberLoginVO;
import com.yyl.crowd.entity.vo.OrderProjectVO;
import com.yyl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    /**
     * 保存地址
     * @param addressVO
     * @param session
     * @return
     */
    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {

        // 通过远程方法保存地址信息
        mySQLRemoteService.saveAddressRemote(addressVO);

        // 从session域得到当前的orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // 得到当前的回报数量
        Integer returnCount = orderProjectVO.getReturnCount();

        // 再次重定向到确认订单的页面（附带回报数量）
        return "redirect:http://localhost/order/confirm/order/" + returnCount;
    }

    /**
     * 点击结算，进入付款页面
     * @param returnCount
     * @param session
     * @return
     */
    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session) {

        // 1.把接收到的回报数量合并到 Session 域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        orderProjectVO.setReturnCount(returnCount);

        session.setAttribute("orderProjectVO", orderProjectVO);

        // 2.获取当前已登录用户的 id

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        // 3.查询目前的收货地址数据

        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList", list);
        }

        return "confirm_order";

    }


    /**
     * 点击修改数量，重新返回结算页面
     * @return
     */
    @RequestMapping("/confirm/modify/info")
    public String modifyReturnConfirm(HttpSession session){

        session.removeAttribute("orderProjectVO");

        Integer returnId = (Integer) session.getAttribute("returnId");


        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(returnId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO orderProjectVO = resultEntity.getData();

            // 为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        return "confirm_return";

    }




    /**
     * 点击 支持按钮 进入结算页面
     * @param returnId
     * @param session
     * @return
     */
    @RequestMapping("/confirm/return/info/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("returnId") Integer returnId,
            HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(returnId);

        //将returnId保存到session域中，以便后面修改数量
        session.setAttribute("returnId",returnId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO orderProjectVO = resultEntity.getData();

            // 为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        return "confirm_return";
    }


}
