package com.powershop.mapper;

import com.powershop.pojo.LocalMessage;
import com.powershop.pojo.LocalMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocalMessageMapper {
    int countByExample(LocalMessageExample example);

    int deleteByExample(LocalMessageExample example);

    int deleteByPrimaryKey(String txNo);

    int insert(LocalMessage record);

    int insertSelective(LocalMessage record);

    List<LocalMessage> selectByExample(LocalMessageExample example);

    LocalMessage selectByPrimaryKey(String txNo);

    int updateByExampleSelective(@Param("record") LocalMessage record, @Param("example") LocalMessageExample example);

    int updateByExample(@Param("record") LocalMessage record, @Param("example") LocalMessageExample example);

    int updateByPrimaryKeySelective(LocalMessage record);

    int updateByPrimaryKey(LocalMessage record);
}