package com.yyl.crowd.mapper;

import com.yyl.crowd.entity.po.AddressPO;
import com.yyl.crowd.entity.po.AddressPOExample;
import java.util.List;

import com.yyl.crowd.entity.po.AddressPO;
import com.yyl.crowd.entity.po.AddressPOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface AddressPOMapper {
    int countByExample(AddressPOExample example);

    int deleteByExample(AddressPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AddressPO record);

    int insertSelective(AddressPO record);

    List<AddressPO> selectByExample(AddressPOExample example);

    AddressPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AddressPO record, @Param("example") AddressPOExample example);

    int updateByExample(@Param("record") AddressPO record, @Param("example") AddressPOExample example);

    int updateByPrimaryKeySelective(AddressPO record);

    int updateByPrimaryKey(AddressPO record);
}