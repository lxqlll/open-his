package com.lxq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxq.constants.Constants;
import com.lxq.domain.DictData;
import com.lxq.domain.User;
import com.lxq.dto.DictDataDto;
import com.lxq.dto.DictTypeDto;
import com.lxq.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.lxq.mapper.DictDataMapper;
import com.lxq.service.DictDataService;

import java.util.Arrays;
import java.util.List;

import static javafx.scene.input.KeyCode.L;

@Service
public class DictDataServiceImpl implements DictDataService {

    /**
     * 声明DictDataMapper对象
     */
    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public DataGridView listPage(DictDataDto dictDataDto) {
        //实例化创建IPage对象
        IPage<DictData> iPage = new Page<>(dictDataDto.getPageNum(),dictDataDto.getPageSize());
        //实例化创建QueryWrapper对象
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        //eq
        queryWrapper.eq(
                StringUtils.isNotBlank(dictDataDto.getDictType()),
                DictData.COL_DICT_TYPE,
                dictDataDto.getDictType());
        //like
        queryWrapper.like(
                StringUtils.isNotBlank(dictDataDto.getDictLabel()),
                DictData.COL_DICT_LABEL,
                dictDataDto.getDictLabel()
        );
        //eq
        queryWrapper.eq(
                StringUtils.isNotBlank(dictDataDto.getStatus()),
                DictData.COL_STATUS,
                dictDataDto.getStatus()
        );
        //order
       // queryWrapper.orderByDesc(DictData.COL_DICT_SORT);
        //获取数据
        List<DictData> list =
                dictDataMapper.selectPage(iPage, queryWrapper).getRecords();

        return new DataGridView(null==list?0l:list.size(),list);
    }

    @Override
    public int insert(DictDataDto dictDataDto) {
        //实例化创建DictData对象
        DictData dictData = new DictData();
        //copy
        BeanUtils.copyProperties(dictDataDto,dictData);
        return dictDataMapper.insert(dictData);
    }

    @Override
    public int deleteDictDataByIds(Long[] dictCodeIds) {
        return dictDataMapper.deleteBatchIds(Arrays.asList(dictCodeIds));
    }

    @Override
    public int update(DictDataDto dictDataDto) {
        //实例化创建DictData对象
        DictData dictData = new DictData();
        //copy
        BeanUtils.copyProperties(dictDataDto,dictData);
        return dictDataMapper.updateById(dictData);
    }

    @Override
    public List<DictData> selectDictDataByDictType(String dictType) {
        //实例化创建QueryWrapper对象
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        //eq
        queryWrapper.eq(StringUtils.isNotBlank(dictType),DictData.COL_DICT_TYPE,dictType);
        //order
        queryWrapper.orderByDesc(DictData.COL_DICT_SORT);
        //eq
        queryWrapper.eq(
                StringUtils.isNotBlank(dictType),
                DictData.COL_STATUS,
                Constants.STATUS_TRUE
        );
        return dictDataMapper.selectList(queryWrapper);
    }

    @Override
    public DictData selectDictDataById(Long dictCode) {
        //实例化创建QueryWrapper对象
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        //eq
        queryWrapper.eq(dictCode!=null,DictData.COL_DICT_CODE,dictCode);

        return dictDataMapper.selectOne(queryWrapper);
    }
}

