package com.lxq.service;

import com.lxq.domain.DictData;
import com.lxq.domain.DictType;
import com.lxq.dto.DictDataDto;
import com.lxq.dto.DictTypeDto;
import com.lxq.vo.DataGridView;

import java.util.List;

/**
 *字典数据Service
 */
public interface DictDataService {

    /**
     * 分页查询字典类型
     * @param dictDataDto
     * @return
     */
    DataGridView listPage(DictDataDto dictDataDto);

    /**
     * 插入新的字典类型
     * @param dictDataDto
     * @return
     */
    int insert(DictDataDto dictDataDto);

    /**
     * 根据ID删除字典类型
     * @param dictCodeIds
     * @return
     */
    int deleteDictDataByIds(Long[] dictCodeIds);


    /**
     * 插入新的字典类型
     * @param dictDataDto
     * @return
     */
    int update(DictDataDto dictDataDto);


    /**
     * 根据字典类型查询字典数据
     * @return
     */
    List<DictData> selectDictDataByDictType(String dictType) ;


    /**
     * 根据ID查询一个字典数据
     * @param dictCode
     * @return
     */
    DictData selectDictDataById(Long dictCode);

}

