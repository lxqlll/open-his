package com.lxq.controller.system;

import cn.hutool.core.date.DateUtil;
import com.lxq.constants.HttpStatus;
import com.lxq.domain.DictData;
import com.lxq.dto.DictDataDto;
import com.lxq.service.DictDataService;
import com.lxq.utils.ShiroSecurityUtils;
import com.lxq.vo.AjaxResult;
import com.lxq.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 字典数据字典控制层
 * @author lxq
 * @date 二〇二〇年九月二十三日 15:28:03
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/system/dict/data")
@Log4j2
public class DictDataController {

    /**
     * 声明dictDataService对象
     */
    @Autowired
    private DictDataService dictDataService;
    /**
     * 由于统一返回类型格式有几种：
     * { "code":200,"msg":"查询成功","data":{"data":"5"} }
     * { "code":200,"msg":"查询成功","size":"5" }
     * 本人习惯取值data 创建map放在data里面
     * 说明：创建Map会消耗一定的空间
     * 定义全局变量遇到过数据还是保留上次数据问题，特意写了getResultMap方法
     */
   /* Map<String,Object> resultMap = Collections.emptyMap();
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
        if(obejct instanceof DictData){
            DictData DictData = objectMapper.convertValue(obejct, DictData.class);
            resultMap.put("DictData",DictData);
        }
        return resultMap;
    }
*/




    /**
     * 分页查询
     * @param DictDataDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/pageForList",method = RequestMethod.POST)
    public AjaxResult pageForList(@RequestBody DictDataDto DictDataDto){
        try{
            //调用分页方法
            DataGridView dataGridView = this.dictDataService.listPage(DictDataDto);

            return AjaxResult.success("查询成功",dataGridView);
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
     * @param dictDataDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/addDictData",method = RequestMethod.POST)
    public AjaxResult addDictData(@RequestBody @Validated DictDataDto dictDataDto){
        try{
            //设置新增人
            dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            //time
            dictDataDto.setCreateTime(DateUtil.date());
            //声明AjaxResult对象
            AjaxResult ajaxResult = null;
            //判断
            if(dictDataService.insert(dictDataDto) == 1){
                ajaxResult = new AjaxResult(HttpStatus.SUCCESS,"新增成功");
            }else{
                ajaxResult = new AjaxResult(HttpStatus.ERROR,"新增失败");
            }
            return ajaxResult;
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
     * 查询所有有效数据字典根据类型
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/getDictDataByDictType/{type}",method = RequestMethod.GET)
    public AjaxResult getDictDataByDictType(@PathVariable
                                                @NotNull(message = "字典数据型不能为空") String type){
        try{
            List<DictData> dictData = this.dictDataService.selectDictDataByDictType(type);
            return new AjaxResult(HttpStatus.SUCCESS,"查询成功",dictData);
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
     * @param dictDataDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/updateDictData",method = RequestMethod.PUT)
    public AjaxResult updateDictData(@RequestBody @Validated DictDataDto dictDataDto){
        try{
            dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            dictDataDto.setUpdateTime(DateUtil.date());
            dictDataService.update(dictDataDto);
            return  new AjaxResult(HttpStatus.SUCCESS,"修改成功");
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
    @RequestMapping(value = "/delDictDataByIds",method = RequestMethod.DELETE)
    public AjaxResult delDictDataByIds(@RequestBody @NotEmpty(message = "删除数据ID不能为空") Long[] ids){
        try{
            this.dictDataService.deleteDictDataByIds(ids);
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
    @RequestMapping(value = "/getDictDataById/{id}",method = RequestMethod.GET)
    public AjaxResult selectDictDataById(@PathVariable @Validated @NotNull(message = "字典数据ID不能不为空") Long id){
        try{
            DictData DictData = this.dictDataService.selectDictDataById(id);
            return new AjaxResult(HttpStatus.SUCCESS,"查询成功",DictData);
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
