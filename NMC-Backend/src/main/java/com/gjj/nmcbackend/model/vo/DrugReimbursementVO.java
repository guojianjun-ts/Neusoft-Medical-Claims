package com.gjj.nmcbackend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class DrugReimbursementVO {
    /**
     * 药品报销级别
     */
    private String drugReimbursementType;

    /**
     * 药品报销比例
     */
    private Integer drugReimbursementProportion;

    /**
     * 状态：1-正常 0-禁用
     */
    private Integer drugStatus;
}
