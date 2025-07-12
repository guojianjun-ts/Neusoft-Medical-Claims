package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.entity.HospitalReimbursement;
import com.gjj.nmcbackend.model.vo.HospitalReimbursementVO;
import java.util.List;

public interface HospitalReimbursementService extends IService<HospitalReimbursement> {

    /**
     * 按医院等级查询（返回VO列表）
     */
    List<HospitalReimbursementVO> getVOByHospitalLevel(String hospitalLevel);

    /**
     * 新增或更新报销比例
     */
    boolean saveOrUpdateReimbursement(HospitalReimbursement entity);
}