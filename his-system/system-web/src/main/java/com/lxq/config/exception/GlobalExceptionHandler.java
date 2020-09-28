package com.lxq.config.exception;

import com.lxq.vo.AjaxResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * 全局异常
 * @author 18249
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 当系统出现MethodArgumentNotValidException这个异常时，会调用下面的方法 MethodArgumentNotValidException
     * 只能用于JSON
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AjaxResult jsonErrorHandler(MethodArgumentNotValidException e){
        return getAjaxResult(e.getBindingResult());
    }

    /**
     * 当系统出现MethodArgumentNotValidException这个异常时，会调用下面的方法 MethodArgumentNotValidException
     * 只能用于JSON
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public AjaxResult jsonErrorHandler(BindException e){
        return getAjaxResult(e.getBindingResult());
    }

    /**
     * 抽象出来的方法
     * @param bindingResult2
     * @return
     */
    private AjaxResult getAjaxResult(BindingResult bindingResult2) {
        /**
         * 调用getBindingResult方法
         */
        BindingResult bindingResult = bindingResult2;
        /**
         * 获取空属性
         */
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        /**
         * 实例化创建ArrayList对象
         */
        List<Map<String, Object>> list = new ArrayList<>();
        /**
         * 创建空集合
         */
        Map<String, Object> map = Collections.emptyMap();
        if (null != allErrors && allErrors.size() > 0) {
            for (ObjectError objectError : allErrors) {
                map = new HashMap<>();
                map.put("defaultMessage:", objectError.getDefaultMessage());
                map.put("objectName:", objectError.getObjectName());
                FieldError fieldError = (FieldError) objectError;
                map.put("field:", fieldError.getField());
                list.add(map);
            }
        }
        /**
         * 如果list为空销毁实例
         */
        if (list.isEmpty() && list.size() == 0) {
            list = Collections.emptyList();
        }
        return AjaxResult.fail("后台数据验证异常", list);
    }


}
