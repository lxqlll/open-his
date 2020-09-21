package com.lxq.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @describe 构造菜单返回给前台的vo
 * @author 1824992529
 * @time 2020年8月29日
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {
    /**
     * 编号
     */
    private String id;
    /**
     * 菜单表 url
     */
    private String serPath;
    /**
     * 是否显示
     */
    private boolean show = true;

    public MenuTreeVo(String id, String serPath) {
        this.id = id;
        this.serPath = serPath;
    }
}
