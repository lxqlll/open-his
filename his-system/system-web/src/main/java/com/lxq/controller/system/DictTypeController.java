package com.lxq.controller.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxq.constants.HttpStatus;
import com.lxq.domain.DictType;
import com.lxq.dto.DictTypeDto;
import com.lxq.service.DictTypeService;
import com.lxq.utils.ShiroSecurityUtils;
import com.lxq.vo.AjaxResult;
import com.lxq.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @deprecated 数据字典控制层
 * @author lxq
 * @date 二〇二〇年九月二十三日 15:28:03
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/system/dict/type")
@Log4j2
public class DictTypeController {

    /**
     * 声明DictTypeService对象
     */
    @Autowired
    private DictTypeService dictTypeService;
    /**
     * 由于统一返回类型格式有几种：
     * { "code":200,"msg":"查询成功","data":{"data":"5"} }
     * { "code":200,"msg":"查询成功","size":"5" }
     * 本人习惯取值data 创建map放在data里面
     * 说明：创建Map会消耗一定的空间
     * 定义全局变量遇到过数据还是保留上次数据问题，特意写了getResultMap方法
     */
    Map<String,Object> resultMap = Collections.emptyMap();
    public Map<String,Object> getResultMap(Object obejct){
        //实例化创建LinkedHashMap集合
        resultMap = new LinkedHashMap<String,Object>(2);
        if(null!=resultMap & resultMap.size()>0){
            resultMap.clear();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        if(obejct instanceof DataGridView){
            DataGridView dataGridView = objectMapper.convertValue(obejct, DataGridView.class);
            resultMap.put("data",dataGridView.getData());
            resultMap.put("total",dataGridView.getTotal());
        }
        if(obejct instanceof DictType){
            DictType dictType = objectMapper.convertValue(obejct, DictType.class);
            resultMap.put("dictType",dictType);
        }
        return resultMap;
    }



     /*try{

    }catch (Exception e){
        //当前类名
        String calssName = this.getClass().getName();
        //当前方法名称
        String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
        //错误日志
        log.error(calssName+"/"+ethodName+":"+e);

        return new AjaxResult(HttpStatus.ERROR,"系统异常");
    }*/


    /**
     * 分页查询
     * @param dictTypeDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/pageForList",method = RequestMethod.POST)
    public AjaxResult pageForList(@RequestBody DictTypeDto dictTypeDto){
        try{
            //调用分页方法
            DataGridView dataGridView = this.dictTypeService.listPage(dictTypeDto);
            //调用getResultMap方法获取集合
            return AjaxResult.success("查询成功",getResultMap(dataGridView));
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    /**
     * 添加数据字典
     * @param dictTypeDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/addDictType",method = RequestMethod.POST)
    public AjaxResult addDictType(@RequestBody DictTypeDto dictTypeDto){
        try{
            //检查字典类型是否存在 类型 名称
            if(this.dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(),dictTypeDto.getDictType())) {
                return AjaxResult.fail("新增数据字典[" + dictTypeDto.getDictName() + "]失败，数据类型已经存在");
            }else{
                dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
                dictTypeService.insert(dictTypeDto);
                return  new AjaxResult(HttpStatus.SUCCESS,"新增成功");
            }
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    /**
     * 查询所有有效数据字典
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public AjaxResult list(){
        try{
            DataGridView dataGridView = this.dictTypeService.list();
            return new AjaxResult(HttpStatus.SUCCESS,"查询成功",getResultMap(dataGridView));
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    /**
     * 修改数据字典
     * @param dictTypeDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/updateDictType",method = RequestMethod.PUT)
    public AjaxResult updateDictType(@RequestBody DictTypeDto dictTypeDto){
        try{
            //编号
            long id = dictTypeDto.getDictId();
            //类型
            String type = dictTypeDto.getDictType();
            //名称
            String name = dictTypeDto.getDictName();
            //检查字典类型是否存在
            if(this.dictTypeService.checkDictTypeUnique(id,type)) {
                return AjaxResult.fail("修改数据字典[" + name + "]失败，数据类型已经存在");
            }else{
                dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
                dictTypeService.update(dictTypeDto);
                return  new AjaxResult(HttpStatus.SUCCESS,"修改成功");
            }
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }



    /**
     * 删除数据字典
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/delDictTypeByIds",method = RequestMethod.DELETE)
    public AjaxResult delDictTypeByIds(@RequestBody @NotEmpty(message = "删除ID不能为空") Long[] ids){
        try{
            this.dictTypeService.deleteDictTypeByIds(ids);
            return new AjaxResult(HttpStatus.SUCCESS,"删除成功");
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }


    /**
     * 编号查询
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/getDictTypeById/{id}",method = RequestMethod.GET)
    public AjaxResult selectDictTypeById(@PathVariable @Validated @NotNull(message = "字典ID不能不为空") Long id){
        try{
            DictType dictType = this.dictTypeService.selectDictTypeById(id);
            return new AjaxResult(HttpStatus.SUCCESS,"查询成功",dictType);
        }catch (Exception e){
            //当前类名
            String calssName = this.getClass().getName();
            //当前方法名称
            String ethodName =  Thread.currentThread().getStackTrace()[1].getMethodName();
            //错误日志
            log.error(calssName+"/"+ethodName+":"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }



}
