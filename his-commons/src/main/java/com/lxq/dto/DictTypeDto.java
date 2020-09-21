package com.lxq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @describe 字典数据输出对象
 * @author 18249992529
 * @time 2020年9月21日
 * @version 1.0
 */
@ApiModel(value="com-lxq-domain-DictType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictTypeDto extends BaseDto{
    /**
     * 字典主键
     */
    @ApiModelProperty(value="字典主键")
    private Long dictId;

    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    @ApiModelProperty(value="字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空")
    @ApiModelProperty(value="字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value="状态（0正常 1停用）")
    private String status;

    /**
     * 创建者
     */
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value="更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;


}
