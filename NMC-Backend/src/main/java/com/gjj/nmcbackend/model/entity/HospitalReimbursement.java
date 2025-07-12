package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 医院报销等级表
 * @TableName hospital_reimbursement
 */
@TableName(value ="hospital_reimbursement")
@Data
public class HospitalReimbursement {
    /**
     * 医院报销 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 医院报销起付金额
     */
    private Integer minPayLevel;

    /**
     * 医院报销等级线
     */
    private Integer maxPayLevel;

    /**
     * 人员类别(1在职人员，0退休人员)
     */
    private Integer peopleType;

    /**
     * 医院报销比例
     */
    private Integer payProportion;

    /**
     * 医院等级
     */
    private String hospitalLevel;

    /**
     * 状态：1-正常 0-禁用
     */
    private Integer status;
}