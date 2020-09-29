package com.lxq.controller.system;

import com.lxq.constants.HttpStatus;
import com.lxq.domain.DictType;
import com.lxq.dto.DictTypeDto;
import com.lxq.service.DictTypeService;
import com.lxq.utils.ShiroSecurityUtils;
import com.lxq.vo.AjaxResult;
import com.lxq.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @description 数据字典控制层
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
        if(obejct instanceof DictType){
            DictType dictType = objectMapper.convertValue(obejct, DictType.class);
            resultMap.put("dictType",dictType);
        }
        return resultMap;
    }
*/




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
     * @param dictTypeDto 字典数据输出对象
     * @return AjaxResult 统一返回类型
     */
    @RequestMapping(value = "/addDictType",method = RequestMethod.POST)
    public AjaxResult addDictType(@RequestBody @Validated DictTypeDto dictTypeDto){
        try{
            //检查字典类型是否存在 类型 名称
            if(this.dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(),dictTypeDto.getDictType())) {
                return AjaxResult.fail("新增数据字典[" + dictTypeDto.getDictName() + "]失败，数据类型已经存在");
            }else{
                //设置新增人
                dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
                //声明AjaxResult对象
                AjaxResult ajaxResult = null;
                //判断
                if(dictTypeService.insert(dictTypeDto) == 1){
                    ajaxResult = new AjaxResult(HttpStatus.SUCCESS,"新增成功");
                }else{
                    ajaxResult = new AjaxResult(HttpStatus.ERROR,"新增失败");
                }
                return ajaxResult;
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
            return new AjaxResult(HttpStatus.SUCCESS,"查询成功",dataGridView);
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
    public AjaxResult updateDictType(@RequestBody @Validated DictTypeDto dictTypeDto){
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


    /**
     * 获取所有的类型
     * @return AjaxResult 统一返回类型
     */
    @GetMapping(value = "/getAllDicType")
    public AjaxResult getAllDicType(){
        try{
            //创建空集合
            //final List<String> typeList = Collections.emptyList();
            //查询所有的数据
            List<DictType> list = (List<DictType>)this.dictTypeService.list().getData();
            //判断是否有数据
            if(null!=list   && list.size()>0){
                //创建list集合
                 List<String>  typeList = new ArrayList<>();
                //循环添加类型
                 list.forEach(item ->{
                    if(null!=item.getDictType() &&  !"".equals(item.getDictType())){
                        typeList.add(item.getDictType());
                    }
                });
                return new AjaxResult(HttpStatus.SUCCESS,"查询成功",typeList);
            }else{
                return new AjaxResult(HttpStatus.SUCCESS,"暂无数据");
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


    @GetMapping(value = "dictCacheAsync")
    public AjaxResult dictCacheAsync(){
        try{
            dictTypeService.dictCacheAsync();
            return AjaxResult.success("同步成功");
        }catch (Exception e){
            e.getStackTrace();
            return AjaxResult.error();
        }
    }


}
