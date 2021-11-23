package com.yyl.crowd.service.api;

import com.yyl.crowd.entity.vo.AddressVO;
import com.yyl.crowd.entity.vo.OrderProjectVO;
import com.yyl.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {

    OrderProjectVO getOrderProjectVO(Integer returnId);


    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
