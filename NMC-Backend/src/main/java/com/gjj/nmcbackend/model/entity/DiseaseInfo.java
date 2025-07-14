package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 疾病信息表
 * @TableName disease_info
 */
@TableName(value ="disease_info")
@Data
public class DiseaseInfo {
    /**
     * 疾病 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 疾病助记编码
     */
    private String diseaseCode;

    /**
     * 疾病名称
     */
    private String diseaseName;

    /**
     * 国际ICD编码
     */
    private String diseaseICD;

    /**
     * 疾病所属分类
     */
    private String diseaseCategory;
}