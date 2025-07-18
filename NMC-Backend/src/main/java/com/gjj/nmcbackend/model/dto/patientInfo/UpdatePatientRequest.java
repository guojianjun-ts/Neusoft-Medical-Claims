
package com.gjj.nmcbackend.model.dto.patientInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 患者信息表
 * @TableName patient_registration
 */
@TableName(value ="patient_registration")
@Data
public class UpdatePatientRequest {

    /**
     * 患者ID
     */
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
     * 出生日期
     */
    private Date birthday;

    /**
     * 年龄
     */
    private Integer page;

    /**
     * 年龄类型（岁、月、日）
     */
    private String ageType;

    /**
     * 住址
     */
    private String homeAddress;


    /**
     * 结算类别（自费、医保）
     */
    private String paymentType;

    /**
     * 工作状态（在职、退休）
     */
    private String workStatus;
}