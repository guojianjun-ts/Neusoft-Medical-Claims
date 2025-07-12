package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 药品报销等级表
 * @TableName drug_reimbursement
 */
@TableName(value ="drug_reimbursement")
@Data
public class DrugReimbursement {
    /**
     * 药品报销 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 药品报销级别
     */
    private String drugReimbursementType;

    /**
     * 药品报销比例
     */
    private Integer drugReimbursementProportion;

    /**
     * 药品报销等级说明
     */
    private String drugReimbursementInfo;

    /**
     * 状态：1-正常 0-禁用
     */
    private Integer drugStatus;
}