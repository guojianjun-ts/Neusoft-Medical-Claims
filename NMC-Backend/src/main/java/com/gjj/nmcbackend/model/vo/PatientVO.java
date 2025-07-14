package com.gjj.nmcbackend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;



/**
 * 病患表格粗略信息
 */
@Data
public class PatientVO {
    /**
     * 患者 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 住院号
     */
    private String caseNumber;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证号
     */
    private String cardNumber;
    /**
     * 入院日期
     */
    private Date visitDate;

    /**
     * 结算类别（自费、医保）
     */
    private String paymentType;

    /**
     * 工作状态（在职、退休）
     */
    private String workStatus;
}
