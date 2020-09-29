package com.lxq.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxq.constants.Constants;
import com.lxq.domain.DictData;
import com.lxq.domain.DictType;
import com.lxq.dto.DictTypeDto;
import com.lxq.mapper.DictDataMapper;
import com.lxq.mapper.DictTypeMapper;
import com.lxq.service.DictTypeService;
import com.lxq.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class DictTypeServiceImpl implements DictTypeService{

    /**
     * 声明DictTypeMapper对象
     */
    @Autowired
    private DictTypeMapper dictTypeMapper;

    /**
     * 声明DictDataMapper对象
     */
    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * spring封装redis对象
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {
        //实例化创建DataGridView对象
        DataGridView dataGridView = new DataGridView();
        //当前页
        Integer pageNum  = dictTypeDto.getPageNum();
        //每页显示条数
        Integer pageSize = dictTypeDto.getPageSize();
        //分页对象
        Page<DictType> page = new Page<DictType>(pageNum,pageSize);
        //实例化创建QueryWrapper对象
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<DictType>();
        /**
         *   添加条件1 名称
         */
        String dictName =  dictTypeDto.getDictName();
        queryWrapper.like(StringUtils.isNoneBlank(dictName),DictType.COL_DICT_NAME,dictName);
        /**
         *   添加条件2 类型
         */
        String dictType =  dictTypeDto.getDictType();
        queryWrapper.like(StringUtils.isNoneBlank(dictType),DictType.COL_DICT_TYPE,dictType);
        /**
         *   添加条件3 状态
         */
        String status =  dictTypeDto.getStatus();
        queryWrapper.eq(StringUtils.isNoneBlank(status),DictType.COL_STATUS,status);
        /**
         *   添加条件4 大于当前时间
         */
        Date beginTime = dictTypeDto.getBeginTime();
        queryWrapper.ge(beginTime!=null,DictType.COL_CREATE_TIME,beginTime);
        /**
         *   添加条件4 小于当前时间
         */
        Date endTime = dictTypeDto.getEndTime();
        queryWrapper.le(endTime!=null,DictType.COL_CREATE_TIME,endTime);
        /**
         * 创建时间排序
         */
        queryWrapper.orderByDesc(DictType.COL_CREATE_TIME);
        //查询数据
        List<DictType>  list = dictTypeMapper.selectPage(page, queryWrapper).getRecords();
        //赋值数据
        dataGridView.setData(list);
        //赋值条数
        dataGridView.setTotal(Long.valueOf(list.size()));
        return dataGridView;
    }

    @Override
    public DataGridView list()  {
        //实例化创建QueryWrapper对象
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        //添加条件2 类型 状态（0正常 1停用）
        queryWrapper.eq(DictType.COL_STATUS,0);
        //查询
        List<DictType> dictTypeList = dictTypeMapper.selectList(queryWrapper);

        //实例化创DictTypeDto类型List集合
       /* List<DictTypeDto> dictTypeDtoList = new ArrayList<>();
        //循环切换数据
        dictTypeList.forEach(item->{
            //实例化创建DictTypeDto对象
            DictTypeDto dictTypeDto = new DictTypeDto();
            //数据实体传输
            BeanUtils.copyProperties(item, dictTypeDto);
            dictTypeDtoList.add(dictTypeDto);
        });*/
        return new DataGridView(null,dictTypeList);
    }

    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        //判断编号是否为空
        dictId = (dictId == null) ? -1L : dictId;
        //实例化创建QueryWrapper对象
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        //添加查询条件
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        //查询数据
        DictType sysDictType = dictTypeMapper.selectOne(qw);
        //判断是否为空
        if(null!=sysDictType && dictId.longValue()!=sysDictType.getDictId().longValue()){
            return true; //说明不存在
        }
        return false; //说明存在
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        //实例化创建DictType对象
        DictType dictType = new DictType();
        //数据实体传输
        BeanUtils.copyProperties(dictTypeDto, dictType);
        //新增时间
        dictType.setCreateTime(DateUtil.date());
        //创建者
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        //新增
        int result = this.dictTypeMapper.insert(dictType);
        return result;
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        //实例化创建DictType对象
        DictType dictType = new DictType();
        //数据实体传输
        BeanUtils.copyProperties(dictTypeDto, dictType);
        //创建者
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        //修改
        int result = this.dictTypeMapper.updateById(dictType);
        return result;
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        int result = 0;
        if (null!=dictIds){
            //删除
            result =  this.dictTypeMapper.deleteBatchIds(Arrays.asList(dictIds));
        }
        return result;
    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        DictType dictType = this.dictTypeMapper.selectById(dictId);
        return dictType;
    }

    /**
     * 同步缓存
     */
    @Override
    public void dictCacheAsync() {
        //实例化创建QueryWrapper对象
        QueryWrapper<DictType> queryWrapper = new QueryWrapper();
        //eq
        queryWrapper.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        //list
        List<DictType> list = dictTypeMapper.selectList(queryWrapper);
        //
        list.forEach(item->{
            //实例化创建QueryWrapper对象
            QueryWrapper<DictData> qw = new QueryWrapper<>();
            //eq
            qw.eq(
                    StringUtils.isNotBlank(item.getDictType()),
                    DictData.COL_DICT_TYPE,
                    item.getDictType());
            //eq
            qw.eq(
                    DictData.COL_STATUS,
                    Constants.STATUS_TRUE
            );
            //order
            queryWrapper.orderByDesc(DictData.COL_DICT_SORT);
            //
            List<DictData> dictDatas = dictDataMapper.selectList(qw);
            //转化为String字符串
            String json = JSON.toJSONString(dictDatas);
            //redis获取ValueOperations对象
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            //存redis
            operations.set(Constants.DICT_REDIS_PROFIX+item.getDictType(),json);
        });
    }

    /*@Override
    public Integer selectCount(DictTypeDto dictTypeDto) {
        //实例化创建QueryWrapper对象
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<DictType>();
        *//**
         *   添加条件1 名称
         *//*
        String dictName =  dictTypeDto.getDictName();
        queryWrapper.like(StringUtils.isNoneBlank(dictName),DictType.COL_DICT_NAME,dictName);
        *//**
         *   添加条件2 类型
         *//*
        String dictType =  dictTypeDto.getDictType();
        queryWrapper.like(StringUtils.isNoneBlank(dictType),DictType.COL_DICT_TYPE,dictType);
        *//**
         *   添加条件3 状态
         *//*
        String status =  dictTypeDto.getStatus();
        queryWrapper.eq(StringUtils.isNoneBlank(status),DictType.COL_STATUS,status);
        *//**
         *   添加条件4 大于当前时间
         *//*
        Date beginTime = dictTypeDto.getBeginTime();
        queryWrapper.ge(beginTime!=null,DictType.COL_CREATE_TIME,beginTime);
        *//**
         *   添加条件4 小于当前时间
         *//*
        Date endTime = dictTypeDto.getEndTime();
        queryWrapper.le(endTime!=null,DictType.COL_CREATE_TIME,endTime);
        return dictTypeMapper.selectCount(queryWrapper);
    }*/

}
