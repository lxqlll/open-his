package com.lxq.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 分页
 */
@ApiModel(value="com-lxq-vo-DataGridView")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView implements Serializable {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 当前分页的集合
     */
    private List<?> data;
}
