package com.gjj.nmcbackend.model.vo;

import com.gjj.nmcbackend.model.entity.HospitalReimbursement;
import lombok.Data;

/**
 * 前端展示用的VO对象
 */
@Data
public class HospitalReimbursementVO {
    private Integer id;
    private String payRange;        // 展示用："起付线-封顶线"格式
    private String peopleTypeDesc;  // 人员类型描述
    private String proportionDesc;  // 比例描述
    private String statusDesc;      // 状态描述

    // 可添加静态工厂方法
    public static HospitalReimbursementVO fromEntity(HospitalReimbursement entity) {
        HospitalReimbursementVO vo = new HospitalReimbursementVO();
        vo.setId(entity.getId());
        vo.setPayRange(entity.getMinPayLevel() + " ~ " + entity.getMaxPayLevel());
        vo.setPeopleTypeDesc(entity.getPeopleType() == 0 ? "退休人员" : "在职人员");
        vo.setProportionDesc(entity.getPayProportion() + "%");
        vo.setStatusDesc(entity.getStatus() == 0 ? "禁用" : "正常");
        return vo;
    }
}