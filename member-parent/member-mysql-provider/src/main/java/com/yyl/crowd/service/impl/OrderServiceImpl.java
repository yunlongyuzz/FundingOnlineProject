package com.yyl.crowd.service.impl;

import com.yyl.crowd.entity.po.AddressPO;
import com.yyl.crowd.entity.po.AddressPOExample;
import com.yyl.crowd.entity.po.OrderPO;
import com.yyl.crowd.entity.po.OrderProjectPO;
import com.yyl.crowd.entity.vo.AddressVO;
import com.yyl.crowd.entity.vo.OrderProjectVO;
import com.yyl.crowd.entity.vo.OrderVO;
import com.yyl.crowd.mapper.AddressPOMapper;
import com.yyl.crowd.mapper.OrderPOMapper;
import com.yyl.crowd.mapper.OrderProjectPOMapper;
import com.yyl.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;
    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressList = addressPOMapper.selectByExample(example);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);

            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO address = new AddressPO();
        BeanUtils.copyProperties(addressVO, address);
        addressPOMapper.insert(address);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProjectPO orderProject = new OrderProjectPO();

        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProject);

        orderPOMapper.insert(orderPO);
        Integer id = orderPO.getId();
        orderProject.setOrderId(id);
        orderProjectPOMapper.insert(orderProject);
    }


}
